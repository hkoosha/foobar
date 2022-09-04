package io.koosha.foobar.maker.api.svc.cmd


import io.koosha.foobar.connect.marketplace.generated.api.ApiResponse
import io.koosha.foobar.connect.marketplace.generated.api.LineItemRequest
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItem
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItemApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.firstOrDef
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.*


@Component
class LineItemCmd(
    private val lineItemApi: OrderRequestLineItemApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = KotlinLogging.logger {}

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

        else -> log.error { "unknown lineItem command: ${freeArgs[0]}" }
    }

    fun postLineItem(
        args: ApplicationArguments,
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val orderRequestId: UUID =
            this.entityIdService.findUUIDOrLast(OrderRequestApi.ENTITY_TYPE, freeArgs.firstOrNull())
        val productId: UUID =
            this.entityIdService.findUUIDOrLast(ProductApi.ENTITY_TYPE, freeArgs.getOrNull(1))

        val req = LineItemRequest()
        req.productId = productId
        req.units = args.firstOrDef("units", "3").toLong()

        if (doLog)
            log.info { "request:\n$req" }
        val response = this.lineItemApi.postLineItemWithHttpInfo(orderRequestId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        if (doLog)
            log.info { "posted lineItem:\n${response.headers}\n$entity" }
    }

    fun getLineItem(
        freeArgs: List<String>,
    ) {

        val orderRequestId: UUID =
            this.entityIdService.findUUIDOrLast(OrderRequestApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val response = this.lineItemApi.getLineItemsWithHttpInfo(orderRequestId)
        assertStatusCode(response.statusCode)
        val all = response.data

        this.log.info { "line-items:\n$all" }

    }

    fun getLastOrderRequestLineItems(doLog: Boolean): List<OrderRequestLineItem> {

        val productId: UUID = this.entityIdService.findUUIDOrLast(ProductApi.ENTITY_TYPE, null)
        val response: ApiResponse<MutableList<OrderRequestLineItem>> =
            this.lineItemApi.getLineItemsWithHttpInfo(productId)
        assertStatusCode(response.statusCode)
        val entities: List<OrderRequestLineItem> = response.data
        if (doLog)
            log.info { "line items:\n${response.headers}\n$entities" }
        return entities
    }

    fun postLineItem(
        orderRequestId: UUID,
        productId: UUID,
        units: Long,
    ): OrderRequestLineItem {

        val req = LineItemRequest()
        req.productId = productId
        req.units = units

        val response = this.lineItemApi.postLineItemWithHttpInfo(orderRequestId, req)
        assertStatusCode(response.statusCode)
        return response.data!!
    }
}
