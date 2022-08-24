package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class OrderRequestServiceLineItemDeletionImpl(
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ) {

        val orderRequest: OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)

        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug(
                "refused to delete lineItem in current state of orderRequest, orderRequest={}, lineItemId={}",
                v("orderRequest", orderRequest),
                v("lineItemId", lineItemId),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not delete line item",
            )
        }

        val lineItem = this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest))

        if (lineItem.isPresent) {
            log.info(
                "removing line item, orderRequest={} lineItem={}",
                v("orderRequest", orderRequest),
                v("lineItem", lineItem.get()),
            )
            this.lineItemRepo.delete(lineItem.get())
        }
        else {
            log.debug(
                "not removing line item as it is not present, orderRequest={} lineItemId={}",
                v("orderRequest", orderRequest),
                v("lineItemId", lineItemId),
            )
        }
    }

}
