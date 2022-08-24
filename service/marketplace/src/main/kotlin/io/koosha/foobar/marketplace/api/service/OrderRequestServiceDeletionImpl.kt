package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class OrderRequestServiceDeletionImpl(
    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun delete(orderRequestId: UUID) {

        val maybeEntity: Optional<OrderRequestDO> = this.finder.findById(orderRequestId)
        if (!maybeEntity.isPresent) {
            log.debug(
                "not deleting order request, entity does not exist, orderRequestId={}",
                orderRequestId,
                kv("orderRequestId", orderRequestId),
            )
            return
        }

        val orderRequest: OrderRequestDO = maybeEntity.get()

        if (orderRequest.state?.deletionAllowed != true) {
            log.debug(
                "refused to delete orderRequest in current state, orderRequest={}",
                orderRequest,
                kv("orderRequest", orderRequest),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request can not be deleted in its current state"
            )
        }

        log.info("deleting orderRequest and lineItems, orderRequest={}", orderRequest, kv("orderRequest", orderRequest))
        this.lineItemRepo.deleteAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequestId)
        this.orderRequestRepo.delete(orderRequest)
    }

}
