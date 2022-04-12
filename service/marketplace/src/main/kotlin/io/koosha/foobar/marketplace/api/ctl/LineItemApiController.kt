package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.marketplace.API_PATH_PREFIX
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.service.LineItemRequest
import io.koosha.foobar.marketplace.api.service.LineItemUpdateRequest
import io.koosha.foobar.marketplace.api.service.OrderRequestService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse
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
    @ResponseBody
    fun getLineItems(
        @PathVariable
        orderRequestId: UUID,
    ): List<OrderRequestLineItem> = service.getLineItems(orderRequestId).map(::OrderRequestLineItem)

    @GetMapping("/{orderRequestLineItemId}")
    @ResponseBody
    fun getLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
    ): OrderRequestLineItem = OrderRequestLineItem(service.getLineItem(orderRequestId, orderRequestLineItemId))

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postLineItem(
        @PathVariable
        orderRequestId: UUID,
        @Valid
        @RequestBody
        request: LineItemRequest,
        response: HttpServletResponse,
    ): OrderRequestLineItem {

        val entity = this.service.addLineItem(orderRequestId, request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                LineItemApiController::class.java,
                "getLineItem",
                orderRequestId,
                entity.orderRequestLineItemPk.orderRequestLineItemId,
            )
            .buildAndExpand(
                orderRequestId,
                entity.orderRequestLineItemPk.orderRequestLineItemId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return OrderRequestLineItem(entity)
    }

    @PatchMapping("/{orderRequestLineItemId}")
    @ResponseBody
    fun patchLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
        @RequestBody
        request: LineItemUpdateRequest,
    ): OrderRequestLineItem = OrderRequestLineItem(
        service.updateLineItem(
            orderRequestId,
            orderRequestLineItemId,
            request
        )
    )

    @DeleteMapping("/{orderRequestLineItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteLineItem(
        @PathVariable
        orderRequestId: UUID,
        @PathVariable
        orderRequestLineItemId: Long,
    ) = this.service.deleteLineItem(orderRequestId, orderRequestLineItemId)


    data class OrderRequestLineItem(
        val orderRequestId: UUID,
        val lineItemId: Long,
        val productId: UUID,
        val units: Long,
    ) {

        constructor(entity: OrderRequestLineItemDO) : this(
            orderRequestId = entity.orderRequestLineItemPk.orderRequest!!.orderRequestId!!,
            lineItemId = entity.orderRequestLineItemPk.orderRequestLineItemId!!,
            productId = entity.productId!!,
            units = entity.units!!,
        )
    }

}
