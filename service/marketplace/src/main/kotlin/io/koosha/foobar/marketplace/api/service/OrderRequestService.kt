package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import java.util.*


interface OrderRequestService {

    fun findById(orderRequestId: UUID): Optional<OrderRequestDO>

    fun findByIdOrFail(orderRequestId: UUID): OrderRequestDO

    fun findAll(): Iterable<OrderRequestDO>

    fun findAllOrderRequestsOfCustomer(customerId: UUID): Iterable<OrderRequestDO>

    fun create(request: OrderRequestCreateRequest): OrderRequestDO

    fun delete(orderRequestId: UUID)

    fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): OrderRequestDO

    fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestLineItemDO

    fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    )

    fun getLineItems(orderRequestId: UUID): Iterable<OrderRequestLineItemDO>

    fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): OrderRequestLineItemDO

    fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO

}
