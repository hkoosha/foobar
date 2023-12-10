package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.OrderRequestLineItemApi
import io.koosha.foobar.maker.api.firstOrDef
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.svc.EntityIdService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LineItemCmd(
    private val lineItemApi: OrderRequestLineItemApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "line-item"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postLineItem(
            args,
            freeArgs,
        )

        matches("post", freeArgs[0]) -> this.postLineItem(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getLineItem(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("list", freeArgs[0]) -> {
            this.getLastOrderRequestLineItems(true)
            Unit
        }

        else -> log.error("unknown lineItem command: {}", freeArgs[0])
    }

    fun postLineItem(
        args: ApplicationArguments,
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val orderRequestId: UUID =
            this.entityIdService.findUUIDOrLast("order_request", freeArgs.firstOrNull())
        val productId: UUID =
            this.entityIdService.findUUIDOrLast("product", freeArgs.getOrNull(1))

        val req = OrderRequestLineItemApi.CreateDto(
            productId = productId,
            units = args.firstOrDef("units", "3").toLong()
        )

        if (doLog)
            log.info("request:\n{}", req)

        val response = this.lineItemApi.create(orderRequestId, req)

        if (doLog)
            log.info("posted lineItem:\n{}", response)
    }

    fun getLineItem(
        freeArgs: List<String>,
    ) {
        val orderRequestId: UUID = this.entityIdService.findUUIDOrLast("order_request", freeArgs.firstOrNull())

        val response = this.lineItemApi.readAll(orderRequestId)

        this.log.info("line-items:\n{}", response)
    }

    fun getLastOrderRequestLineItems(
        doLog: Boolean,
    ): Collection<OrderRequestLineItemApi.ReadDto> {

        val productId: UUID = this.entityIdService.findUUIDOrLast("product", null)
        val response = this.lineItemApi.readAll(productId)

        if (doLog)
            log.info("line items:\n{}", response)

        return response
    }

    fun postLineItem(
        orderRequestId: UUID,
        productId: UUID,
        units: Long,
    ): OrderRequestLineItemApi.CreateRespDto {

        val req = OrderRequestLineItemApi.CreateDto(
            productId = productId,
            units = units,
        )

        val response = this.lineItemApi.create(orderRequestId, req)

        return response
    }

}
