package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequest
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestCreateRequest
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestUpdateRequest
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.first
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.*


@Component
class OrderRequestCmd(
    private val orderRequestApi: OrderRequestApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = OrderRequestApi.ENTITY_TYPE.replace('_', '-')

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

        else -> log.error { "unknown orderRequest command: ${freeArgs[0]}" }
    }

    fun postOrderRequest(
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = OrderRequestCreateRequest()
        req.customerId = customerId

        if (doLog)
            log.info { "request:\n$req" }
        val response = this.orderRequestApi.postOrderRequestWithHttpInfo(req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val internalId = this.entityIdService
            .findMaxInternalIdByEntityType(OrderRequestApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = entity.orderRequestId.toString(),
                internalId = internalId,
                entityType = OrderRequestApi.ENTITY_TYPE,
            )
        )

        if (doLog)
            log.info { "posted order-request:\n${response.headers}\ninternalId=$internalId\nentity:\n$entity" }
    }

    fun patchOrderRequest(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val orderRequestId = this.entityIdService.findUUIDOrLast(OrderRequestApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = OrderRequestUpdateRequest()
        if (args.firstOrNull("sellerId") != null)
            req.sellerId = UUID.fromString(args.firstOrNull("sellerId"))
        if (args.firstOrNull("state") != null)
            req.state = OrderRequestUpdateRequest.StateEnum.valueOf(args.first("state"))
        req.subTotal = args.firstOrNull("subTotal")?.toLong()

        log.info { "request:\n$req" }
        val response = this.orderRequestApi.patchOrderRequestWithHttpInfo(orderRequestId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data
        log.info { "patched order request:\n${response.headers}\n$entity" }
    }

    fun patchOrderRequest2() {

        val orderRequestId = this.entityIdService.findUUIDOrLast(OrderRequestApi.ENTITY_TYPE, null)

        val req = OrderRequestUpdateRequest()
        req.state = OrderRequestUpdateRequest.StateEnum.LIVE

        log.info { "request:\n$req" }
        val response = this.orderRequestApi.patchOrderRequestWithHttpInfo(orderRequestId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data
        log.info { "patched order request:\n${response.headers}\n$entity" }
    }

    fun getOrderRequest(
        freeArgs: List<String>,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, freeArgs.firstOrNull())
        val orderRequestId: UUID? =
            if (freeArgs.size > 1) {
                this.entityIdService.findUUID(OrderRequestApi.ENTITY_TYPE, freeArgs[1])
            }
            else {
                null
            }

        if (orderRequestId == null) {

            val response = this.orderRequestApi.getOrderRequestsWithHttpInfo(customerId)
            assertStatusCode(response.statusCode)
            val all = response.data
            this.log.info { "order requests:\n${response.headers}\n$all" }

        }
        else {

            val response = this.orderRequestApi.getOrderRequestWithHttpInfo(orderRequestId)
            assertStatusCode(response.statusCode)
            val entity = response.data
            log.info { "customer address:\n${response.headers}\n$entity" }

        }

    }

    fun getLastOrderRequest(doLog: Boolean): OrderRequest? {

        val orderRequestId = this.entityIdService.findUUIDOrLast(OrderRequestApi.ENTITY_TYPE, null)
        val response = this.orderRequestApi.getOrderRequestWithHttpInfo(orderRequestId)
        val entity: OrderRequest? = response.data
        if (doLog)
            log.info { "orderRequest:\n${response.headers}\n$entity" }
        return entity
    }

}
