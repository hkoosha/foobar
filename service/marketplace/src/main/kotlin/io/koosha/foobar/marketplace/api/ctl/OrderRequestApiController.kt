package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.marketplace.API_PATH_PREFIX
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.service.OrderRequestCreateRequest
import io.koosha.foobar.marketplace.api.service.OrderRequestService
import io.koosha.foobar.marketplace.api.service.OrderRequestUpdateRequest
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/$API_PATH_PREFIX/order-requests")
@Tags(
    Tag(name = OrderRequestDO.ENTITY_TYPE_DASHED)
)
class OrderRequestApiController(
    private val service: OrderRequestService,
) {

    @GetMapping
    @ResponseBody
    fun getOrderRequests(
        @RequestParam(required = false)
        customerId: UUID?,
    ): List<OrderRequest> {

        val entities: Iterable<OrderRequestDO> = when (customerId) {
            null -> this.service.findAll()
            else -> this.service.findAllOrderRequestsOfCustomer(customerId)
        }
        return entities.map(::OrderRequest)
    }

    @GetMapping("/{orderRequestId}")
    @ResponseBody
    fun getOrderRequest(
        @PathVariable
        orderRequestId: UUID,
    ): OrderRequest {

        val entity: OrderRequestDO = this.service.findByIdOrFail(orderRequestId)
        return OrderRequest(entity)
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postOrderRequest(
        @RequestBody
        @Valid
        request: OrderRequestCreateRequest,
        response: HttpServletResponse,
    ): OrderRequest {

        val entity: OrderRequestDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                OrderRequestApiController::class.java,
                "getOrderRequest",
                entity.orderRequestId,
            )
            .buildAndExpand(
                entity.orderRequestId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return OrderRequest(entity)
    }

    @PatchMapping("/{orderRequestId}")
    @ResponseBody
    fun patchOrderRequest(
        @PathVariable
        orderRequestId: UUID,
        @RequestBody
        request: OrderRequestUpdateRequest,
    ): OrderRequest {

        val entity: OrderRequestDO = this.service.update(orderRequestId, request)
        return OrderRequest(entity)
    }

    @DeleteMapping("/{orderRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrderRequest(
        @PathVariable
        orderRequestId: UUID,
    ) = this.service.delete(orderRequestId)


    data class OrderRequest(

        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: OrderRequestState,
        val subTotal: Long?,
    ) {

        constructor(entity: OrderRequestDO) : this(

            orderRequestId = entity.orderRequestId!!,
            customerId = entity.customerId!!,
            sellerId = entity.sellerId,
            state = entity.state!!,
            subTotal = entity.subTotal,
        )
    }

}
