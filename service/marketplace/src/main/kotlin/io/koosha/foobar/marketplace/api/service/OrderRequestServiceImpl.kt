package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    override fun findById(orderRequestId: UUID): Mono<OrderRequestDO> = this.finder.findById(orderRequestId)

    override fun findByIdOrFail(orderRequestId: UUID): Mono<OrderRequestDO> = this.finder.findByIdOrFail(orderRequestId)

    override fun findAll(): Flux<OrderRequestDO> = this.finder.findAll()

    override fun findAllOrderRequestsOfCustomer(customerId: UUID): Flux<OrderRequestDO> =
        this.finder.findAllOrderRequestsOfCustomer(customerId)

    override fun create(request: OrderRequestCreateRequest): Mono<OrderRequestDO> = this.creator.create(request)

    override fun delete(orderRequestId: UUID): Mono<Void> = this.deleter.delete(orderRequestId)

    override fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): Mono<OrderRequestDO> = this.updater.update(orderRequestId, request)

    override fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): Mono<OrderRequestLineItemDO> = this.lineItemCreator.addLineItem(orderRequestId, request)

    override fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<Void> = this.lineItemDeleter.deleteLineItem(orderRequestId, lineItemId)

    override fun getLineItems(orderRequestId: UUID): Flux<OrderRequestLineItemDO> =
        this.lineItemFinder.getLineItems(orderRequestId)

    override fun getLineItem(orderRequestId: UUID, lineItemId: Long): Mono<OrderRequestLineItemDO> =
        this.lineItemFinder.getLineItem(orderRequestId, lineItemId)

    override fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): Mono<OrderRequestLineItemDO> = this.lineItemUpdater.updateLineItem(orderRequestId, lineItemId, request)

}
