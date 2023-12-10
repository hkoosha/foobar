package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.OrderRequestApi
import io.koosha.foobar.maker.api.first
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OrderRequestCmd(
    private val orderRequestApi: OrderRequestApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "order-request"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postOrderRequest(
            freeArgs,
        )

        matches("post", freeArgs[0]) -> this.postOrderRequest(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("patch", freeArgs[0]) -> this.patchOrderRequest(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getOrderRequest(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("last", freeArgs[0]) -> {
            this.getLastOrderRequest(true)
            Unit
        }

        else -> log.error("unknown orderRequest command: {}", freeArgs[0])
    }

    fun postOrderRequest(
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast("customer", freeArgs.firstOrNull())

        val req = OrderRequestApi.CreateDto(
            customerId = customerId,
        )

        if (doLog)
            log.info("request:\n{}", req)

        val response = this.orderRequestApi.create(req)

        val internalId = this.entityIdService
            .findMaxInternalIdByEntityType("order_request")
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = response.orderRequestId.toString(),
                internalId = internalId,
                entityType = "order_request",
            )
        )

        this.entityIdService.putOrderRequestIntoLineItemWorkQueue(response.orderRequestId)

        if (doLog)
            log.info("posted order-request:\ninternalId={}\nentity:\n{}", internalId, response)
    }

    fun patchOrderRequest(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val orderRequestId = this.entityIdService.findUUIDOrLast("order_request", freeArgs.firstOrNull())

        val req = OrderRequestApi.UpdateDto(
            sellerId = if (args.firstOrNull("sellerId") != null)
                UUID.fromString(args.firstOrNull("sellerId"))
            else
                null,
            state = if (args.firstOrNull("state") != null)
                OrderRequestApi.State.valueOf(args.first("state"))
            else
                null,
            subTotal = args.firstOrNull("subTotal")?.toLong(),
        )

        log.info("request:\n{}", req)

        val response = this.orderRequestApi.update(orderRequestId, req)

        log.info("patched order request:\n{}", response)
    }

    fun patchOrderRequest() {

        val orderRequestId = this.entityIdService.findUUIDOrLast("order_request", null)

        val req = OrderRequestApi.UpdateDto(
            state = OrderRequestApi.State.LIVE,
        )

        log.info("request:\n{}", req)

        val response = this.orderRequestApi.update(orderRequestId, req)

        log.info("patched order request:\n{}", response)
    }

    fun patchOrderRequest(
        orderRequestId: UUID,
    ): OrderRequestApi.UpdateRespDto {

        val req = OrderRequestApi.UpdateDto(
            state = OrderRequestApi.State.LIVE,
        )

        val response = this.orderRequestApi.update(orderRequestId, req)

        return response
    }

    fun getOrderRequest(
        freeArgs: List<String>,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast("customer", freeArgs.firstOrNull())

        val orderRequestId: UUID? =
            if (freeArgs.size > 1)
                this.entityIdService.findUUID("order_request", freeArgs[1])
            else
                null

        if (orderRequestId == null) {

            val response = this.orderRequestApi.readAllForCustomer(customerId)
            log.info("order requests:\n{}", response)

        }
        else {

            val response = this.orderRequestApi.read(orderRequestId)
            log.info("customer address:\n{}", response)

        }

    }

    fun getLastOrderRequest(doLog: Boolean): OrderRequestApi.ReadDto {

        val orderRequestId = this.entityIdService.findUUIDOrLast("order_request", null)
        val response = this.orderRequestApi.read(orderRequestId)

        if (doLog)
            log.info("orderRequest:\n{}", response)

        return response
    }

}
