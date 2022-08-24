package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class OrderRequestServiceLineItemFinderImpl(
    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(readOnly = true)
    fun getLineItems(orderRequestId: UUID): Iterable<OrderRequestLineItemDO> {

        this.finder.findByIdOrFail(orderRequestId)
        val all = this.lineItemRepo.findAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequestId)
        return all
    }

    @Transactional(readOnly = true)
    fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): OrderRequestLineItemDO {

        val orderRequest = this.finder.findByIdOrFail(orderRequestId)
        val lineItem = this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest)).orElseThrow {
            log.trace(
                "lineItem not found, orderRequest={}, lineItemId={}",
                v("orderRequest", orderRequest),
                v("lineItemId", lineItemId),
            )
            EntityNotFoundException(
                entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                entityId = lineItemId,
            )
        }
        return lineItem
    }

}
