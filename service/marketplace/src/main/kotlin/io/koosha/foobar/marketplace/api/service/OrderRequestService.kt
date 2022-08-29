package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


interface OrderRequestService {

    fun findById(orderRequestId: UUID): Mono<OrderRequestDO>

    fun findByIdOrFail(orderRequestId: UUID): Mono<OrderRequestDO>

    fun findAll(): Flux<OrderRequestDO>

    fun findAllOrderRequestsOfCustomer(customerId: UUID): Flux<OrderRequestDO>

    fun create(request: OrderRequestCreateRequest): Mono<OrderRequestDO>

    fun delete(orderRequestId: UUID): Mono<Void>

    fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): Mono<OrderRequestDO>

    fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): Mono<OrderRequestLineItemDO>

    fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<Void>

    fun getLineItems(orderRequestId: UUID): Flux<OrderRequestLineItemDO>

    fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<OrderRequestLineItemDO>

    fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): Mono<OrderRequestLineItemDO>

}
