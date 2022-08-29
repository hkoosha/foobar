package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*


@Service
class OrderRequestServiceDeletionImpl(
    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    fun delete(orderRequestId: UUID): Mono<Void> =
        this.finder
            .findById(orderRequestId)
            .switchIfEmpty(Mono.defer {
                log.debug(
                    "not deleting order request, entity does not exist, orderRequestId={}",
                    v("orderRequestId", orderRequestId),
                )
                Mono.empty()
            })
            .flatMap { orderRequest ->
                if (orderRequest.state?.deletionAllowed != true) {
                    log.debug(
                        "refused to delete orderRequest in current state, orderRequest={}",
                        v("orderRequest", orderRequest),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequestId,
                            msg = "order request can not be deleted in its current state"
                        )
                    )
                }
                else {
                    log.info("deleting orderRequest and lineItems, orderRequest={}", v("orderRequest", orderRequest))
                    val lineItemsDelete = this.lineItemRepo.deleteAllByOrderRequestId(orderRequestId.toString())
                    val orderRequestDelete = this.orderRequestRepo.delete(orderRequest)
                    Mono.zip(lineItemsDelete, orderRequestDelete)
                }
            }
            .flatMap {
                Mono.empty()
            }

}
