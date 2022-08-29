package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*


@Service
class OrderRequestServiceLineItemDeletionImpl(
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): Mono<Void> =
        this.finder
            .findByIdOrFail(orderRequestId)
            .flatMap { orderRequest ->
                if (orderRequest.state != OrderRequestState.ACTIVE) {
                    log.debug(
                        "refused to delete lineItem in current state of orderRequest, orderRequest={}, lineItemId={}",
                        v("orderRequest", orderRequest),
                        v("lineItemId", lineItemId),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequestId,
                            msg = "order request is not active, can not delete line item",
                        )
                    )
                }
                else {
                    this.lineItemRepo
                        .findByOrderRequestIdAndOrderRequestLineItemId(orderRequestId.toString(), lineItemId)
                        .flatMap {
                            log.info(
                                "removing line item, orderRequest={} lineItem={}",
                                v("orderRequest", orderRequest),
                                v("lineItem", it),
                            )
                            this.lineItemRepo.delete(it)
                        }
                        .switchIfEmpty {
                            log.debug(
                                "not removing line item as it is not present, orderRequest={} lineItemId={}",
                                v("orderRequest", orderRequest),
                                v("lineItemId", lineItemId),
                            )
                            Mono.empty()
                        }
                }
            }

}
