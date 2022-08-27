package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.API_PATH_PREFIX
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.service.OrderRequestCreateRequest
import io.koosha.foobar.marketplace.api.service.OrderRequestService
import io.koosha.foobar.marketplace.api.service.OrderRequestUpdateRequest
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/$API_PATH_PREFIX/order-requests")
@Tags(
    Tag(name = OrderRequestDO.ENTITY_TYPE_DASHED)
)
class OrderRequestApiController(
    private val service: OrderRequestService,
) {

    @Suppress("UNCHECKED_CAST")
    private fun <T> err(err: Any) = ResponseEntity
        .badRequest()
        .contentType(MediaType.APPLICATION_JSON)
        // FIXME erm...?!!
        .body(err) as ResponseEntity<T>

    private fun <T> ok(value: T) = ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(value)

    @GetMapping
    fun getOrderRequests(
        @RequestParam(required = false)
        customerId: UUID?,
    ): Mono<ResponseEntity<List<OrderRequest>>> =
        when (customerId) {
            null -> this.service.findAll()
            else -> this.service.findAllOrderRequestsOfCustomer(customerId)
        }
            .map(::OrderRequest)
            .collectList()
            .map {
                this.ok(it)
            }
            .onErrorResume {
                Mono.just(this.err(it))
            }

    @GetMapping("/{orderRequestId}")
    fun getOrderRequest(
        @PathVariable
        orderRequestId: UUID,
    ): Mono<ResponseEntity<OrderRequest>> =
        this.service
            .findByIdOrFail(orderRequestId).map(::OrderRequest)
            .map {
                this.ok(it)
            }
            .onErrorResume {
                Mono.just(this.err(it))
            }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postOrderRequest(
        @RequestBody
        @Valid
        request: OrderRequestCreateRequest,
    ): Mono<ResponseEntity<OrderRequest>> =
        // TODO set http location header.
        this.service
            .create(request)
            .map(::OrderRequest)
            .map {
                this.ok(it)
            }
            .onErrorResume {
                Mono.just(this.err(it))
            }

    @PatchMapping("/{orderRequestId}")
    fun patchOrderRequest(
        @PathVariable
        orderRequestId: UUID,
        @RequestBody
        request: OrderRequestUpdateRequest,
    ): Mono<ResponseEntity<OrderRequest>> =
        this.service
            .update(orderRequestId, request).map(::OrderRequest)
            .map {
                this.ok(it)
            }
            .onErrorResume {
                Mono.just(this.err(it))
            }

    @DeleteMapping("/{orderRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrderRequest(
        @PathVariable
        orderRequestId: UUID,
    ): Mono<ResponseEntity<Void>> =
        this.service
            .delete(orderRequestId)
            .map {
                ResponseEntity
                    .noContent()
                    .build<Void>()
            }
            .onErrorResume {
                Mono.just(this.err(it))
            }

    data class OrderRequest(

        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: OrderRequestState,
        val subTotal: Long?,
    ) {

        constructor(entity: OrderRequestDO) : this(

            orderRequestId = entity.orderRequestId!!.toUUID(),
            customerId = entity.customerId!!.toUUID(),
            sellerId = entity.sellerId?.toUUID(),
            state = entity.state!!,
            subTotal = entity.subTotal,
        )
    }

}
