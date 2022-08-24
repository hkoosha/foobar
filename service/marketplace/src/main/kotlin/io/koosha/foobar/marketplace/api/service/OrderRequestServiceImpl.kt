package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import org.springframework.stereotype.Service
import java.util.*


@Service
final class OrderRequestServiceImpl(
    private val finder: OrderRequestServiceFindingImpl,
    private val creator: OrderRequestServiceCreationImpl,
    private val deleter: OrderRequestServiceDeletionImpl,
    private val updater: OrderRequestServiceUpdaterImpl,
    private val lineItemCreator: OrderRequestServiceLineItemCreatorImpl,
    private val lineItemFinder: OrderRequestServiceLineItemFinderImpl,
    private val lineItemDeleter: OrderRequestServiceLineItemDeletionImpl,
    private val lineItemUpdater: OrderRequestServiceLineItemUpdaterImpl,
) : OrderRequestService {

    override fun findById(orderRequestId: UUID): Optional<OrderRequestDO> = this.finder.findById(orderRequestId)

    override fun findByIdOrFail(orderRequestId: UUID): OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)

    override fun findAll(): Iterable<OrderRequestDO> = this.finder.findAll()

    override fun findAllOrderRequestsOfCustomer(customerId: UUID): Iterable<OrderRequestDO> =
        this.finder.findAllOrderRequestsOfCustomer(customerId)

    override fun create(request: OrderRequestCreateRequest): OrderRequestDO = this.creator.create(request)

    override fun delete(orderRequestId: UUID) = this.deleter.delete(orderRequestId)

    override fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): OrderRequestDO = this.updater.update(orderRequestId, request)

    override fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestLineItemDO = this.lineItemCreator.addLineItem(orderRequestId, request)

    override fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ) = this.lineItemDeleter.deleteLineItem(orderRequestId, lineItemId)

    override fun getLineItems(orderRequestId: UUID): Iterable<OrderRequestLineItemDO> =
        this.lineItemFinder.getLineItems(orderRequestId)

    override fun getLineItem(orderRequestId: UUID, lineItemId: Long): OrderRequestLineItemDO =
        this.lineItemFinder.getLineItem(orderRequestId, lineItemId)

    override fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO = this.lineItemUpdater.updateLineItem(orderRequestId, lineItemId, request)

}
