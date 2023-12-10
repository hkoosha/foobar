package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.dto.LineItemRequestDto
import io.koosha.foobar.marketplace.api.model.dto.LineItemUpdateRequestDto
import io.koosha.foobar.marketplace.api.model.dto.OrderRequestCreateRequestDto
import io.koosha.foobar.marketplace.api.model.dto.OrderRequestUpdateRequestDto
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestLineItemDO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
final class OrderRequestService(
    private val finder: OrderRequestServiceFinding,
    private val creator: OrderRequestServiceCreation,
    private val deleter: OrderRequestServiceDeletion,
    private val updater: OrderRequestServiceUpdater,
    private val lineItemCreator: OrderRequestServiceLineItemCreator,
    private val lineItemFinder: OrderRequestServiceLineItemFinder,
    private val lineItemDeleter: OrderRequestServiceLineItemDeletion,
    private val lineItemUpdater: OrderRequestServiceLineItemUpdater,
) {

    fun findById(
        orderRequestId: UUID,
    ): Mono<OrderRequestDO> = this.finder.findById(orderRequestId)

    fun findByIdOrFail(
        orderRequestId: UUID,
    ): Mono<OrderRequestDO> = this.finder.findByIdOrFail(orderRequestId)

    fun findAll(): Flux<OrderRequestDO> =
        this.finder.findAll()

    fun findAllOrderRequestsOfCustomer(
        customerId: UUID,
    ): Flux<OrderRequestDO> =
        this.finder.findAllOrderRequestsOfCustomer(customerId)

    fun create(
        request: OrderRequestCreateRequestDto,
    ): Mono<OrderRequestDO> =
        this.creator.create(request)

    fun delete(
        orderRequestId: UUID,
    ): Mono<Void> =
        this.deleter.delete(orderRequestId)

    fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequestDto,
    ): Mono<OrderRequestDO> =
        this.updater.update(orderRequestId, request)

    fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequestDto,
    ): Mono<OrderRequestLineItemDO> =
        this.lineItemCreator.addLineItem(orderRequestId, request)

    fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<Void> =
        this.lineItemDeleter.deleteLineItem(orderRequestId, lineItemId)

    fun getLineItems(
        orderRequestId: UUID,
    ): Flux<OrderRequestLineItemDO> =
        this.lineItemFinder.getLineItems(orderRequestId)

    fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<OrderRequestLineItemDO> =
        this.lineItemFinder.getLineItem(orderRequestId, lineItemId)

    fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequestDto,
    ): Mono<OrderRequestLineItemDO> =
        this.lineItemUpdater.updateLineItem(orderRequestId, lineItemId, request)

}
