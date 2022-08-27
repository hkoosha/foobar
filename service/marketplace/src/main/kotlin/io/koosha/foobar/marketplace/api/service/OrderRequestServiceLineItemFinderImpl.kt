package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@Service
class OrderRequestServiceLineItemFinderImpl(
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    fun getLineItems(orderRequestId: UUID): Flux<OrderRequestLineItemDO> =
        this.finder
            .findByIdOrFail(orderRequestId)
            .flux()
            .flatMap {
                this.lineItemRepo.findAllByOrderRequestId(orderRequestId.toString())
            }

    fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<OrderRequestLineItemDO> =
        this.lineItemRepo
            .findByOrderRequestIdAndOrderRequestLineItemId(orderRequestId.toString(), lineItemId)
            .switchIfEmpty(Mono.defer {
                log.trace(
                    "lineItem not found, orderRequest={}, lineItemId={}",
                    v("orderRequestId", orderRequestId),
                    v("lineItemId", lineItemId),
                )
                Mono.error(
                    EntityNotFoundException(
                        entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                        entityId = lineItemId,
                    )
                )
            })

}
