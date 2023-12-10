package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.api.model.dto.LineItemRequestDto
import io.koosha.foobar.marketplace.api.model.dto.LineItemUpdateRequestDto
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.service.OrderRequestService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.validation.Valid
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
import java.util.UUID

@RestController
@RequestMapping("/foobar/marketplace/v1/order-request/{orderRequestId}/line-item")
@Tags(
    Tag(name = "order-request-line-item")
)
class LineItemApiController(
    private val service: OrderRequestService,
) {

    @PostMapping
    fun create(
        @PathVariable
        orderRequestId: UUID,

        @Valid
        @RequestBody
        request: LineItemRequestDto,
    ): Mono<OrderRequestLineItem> =
        // TODO set http location header.
        this.service
            .addLineItem(orderRequestId, request)
            .map(::OrderRequestLineItem)

    @GetMapping
    fun readAll(
        @PathVariable
        orderRequestId: UUID,
    ): Flux<OrderRequestLineItem> =
        this.service
            .getLineItems(orderRequestId)
            .map(::OrderRequestLineItem)

    @GetMapping("/{orderRequestLineItemId}")
    fun read(
        @PathVariable
        orderRequestId: UUID,

        @PathVariable
        orderRequestLineItemId: Long,
    ): Mono<OrderRequestLineItem> =
        this.service
            .getLineItem(orderRequestId, orderRequestLineItemId)
            .map(::OrderRequestLineItem)

    @PatchMapping("/{orderRequestLineItemId}")
    fun update(
        @PathVariable
        orderRequestId: UUID,

        @PathVariable
        orderRequestLineItemId: Long,

        @RequestBody
        request: LineItemUpdateRequestDto,
    ): Mono<OrderRequestLineItem> =
        this.service
            .updateLineItem(
                orderRequestId,
                orderRequestLineItemId,
                request
            )
            .map(::OrderRequestLineItem)

    @DeleteMapping("/{orderRequestLineItemId}")
    fun delete(
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
