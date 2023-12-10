package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestLineItemRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class OrderRequestServiceLineItemFinder(
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val finder: OrderRequestServiceFinding,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

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
                        entityType = "order_request_line_item",
                        entityId = lineItemId,
                    )
                )
            })

}
