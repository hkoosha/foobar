package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.API_PATH_PREFIX
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.service.LineItemRequest
import io.koosha.foobar.marketplace.api.service.LineItemUpdateRequest
import io.koosha.foobar.marketplace.api.service.OrderRequestService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/$API_PATH_PREFIX/order-requests/{orderRequestId}/line-items")
@Tags(
    Tag(name = OrderRequestLineItemDO.ENTITY_TYPE_DASHED)
)
class LineItemApiController(
    private val service: OrderRequestService,
) {

    @GetMapping
    fun getLineItems(
        @PathVariable
        orderRequestId: UUID,
    ): Flux<OrderRequestLineItem> =
        this.service
            .getLineItems(orderRequestId)
            .map(::OrderRequestLineItem)

    @GetMapping("/{orderRequestLineItemId}")
    fun getLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
    ): Mono<OrderRequestLineItem> =
        this.service
            .getLineItem(orderRequestId, orderRequestLineItemId)
            .map(::OrderRequestLineItem)

    @PostMapping
    fun postLineItem(
        @PathVariable
        orderRequestId: UUID,
        @Valid
        @RequestBody
        request: LineItemRequest,
    ): Mono<OrderRequestLineItem> =
        // TODO set http location header.
        this.service
            .addLineItem(orderRequestId, request)
            .map(::OrderRequestLineItem)

    @PatchMapping("/{orderRequestLineItemId}")
    fun patchLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
        @RequestBody
        request: LineItemUpdateRequest,
    ): Mono<OrderRequestLineItem> =
        this.service
            .updateLineItem(
                orderRequestId,
                orderRequestLineItemId,
                request
            )
            .map(::OrderRequestLineItem)

    @DeleteMapping("/{orderRequestLineItemId}")
    fun deleteLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
    ): Mono<Void> =
        this.service.deleteLineItem(orderRequestId, orderRequestLineItemId)

    data class OrderRequestLineItem(
        val orderRequestId: UUID,
        val lineItemId: Long,
        val productId: UUID,
        val units: Long,
    ) {

        constructor(entity: OrderRequestLineItemDO) : this(
            orderRequestId = entity.orderRequestId!!.toUUID(),
            lineItemId = entity.orderRequestLineItemId!!,
            productId = entity.productId!!.toUUID(),
            units = entity.units!!,
        )
    }

}
