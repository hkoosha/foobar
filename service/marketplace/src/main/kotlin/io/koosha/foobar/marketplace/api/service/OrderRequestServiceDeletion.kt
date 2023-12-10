package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class OrderRequestServiceDeletion(
    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val finder: OrderRequestServiceFinding,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

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
                            entityType = "order_request",
                            entityId = orderRequestId,
                            msg = "order request can not be deleted in its current state"
                        )
                    )
                }
                else {
                    log.info("deleting orderRequest and lineItems, orderRequest={}", v("orderRequest", orderRequest))
                    val lineItemsDelete: Mono<Void?> =
                        this.lineItemRepo.deleteAllByOrderRequestId(orderRequestId.toString())
                    val orderRequestDelete: Mono<Void?> =
                        this.orderRequestRepo.delete(orderRequest)
                    Mono.`when`(lineItemsDelete, orderRequestDelete)
                }
            }

}
