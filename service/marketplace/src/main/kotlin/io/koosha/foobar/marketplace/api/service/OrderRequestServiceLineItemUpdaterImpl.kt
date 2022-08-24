package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator


@Service
class OrderRequestServiceLineItemUpdaterImpl(
    private val validator: Validator,

    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    private fun validate(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update lineItem validation error, orderRequestId={} lineItemId={} errors={}",
                v("orderRequestId", orderRequestId),
                v("lineItemId", lineItemId),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }
    }

    private fun findOrderRequest(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestDO {

        val orderRequest: OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)
        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug(
                "refused to update lineItem in current state of orderRequest, " +
                        "orderRequest={}, lineItemId={}, request={}",
                v("orderRequest", orderRequest),
                v("lineItemId", lineItemId),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not modify line item",
            )
        }

        return orderRequest
    }

    private fun findLineItem(
        orderRequest: OrderRequestDO,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO {

        val lineItem: OrderRequestLineItemDO =
            this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest)).orElseThrow {
                log.trace(
                    "lineItem not found, orderRequest={}, lineItemId={}, request={}",
                    v("orderRequest", orderRequest),
                    v("lineItemId", lineItemId),
                    v("request", request),
                )
                EntityNotFoundException(
                    entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                    entityId = lineItemId,
                )
            }

        return lineItem
    }

    private fun saveLineItem(
        orderRequest: OrderRequestDO,
        lineItem: OrderRequestLineItemDO,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO {

        var anyChange = false

        if (request.units != null) {
            lineItem.units = request.units
            anyChange = true
        }

        return if (anyChange) {
            log.info(
                "updating order request line item, orderRequest={}, lineItemId={}, request={}",
                v("orderRequest", orderRequest),
                v("lineItemId", lineItem.orderRequestLineItemPk.orderRequestLineItemId),
                v("request", request),
            )
            val saved: OrderRequestLineItemDO = this.lineItemRepo.save(lineItem)
            saved
        }
        else {
            lineItem
        }
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO {

        this.validate(orderRequestId, lineItemId, request)
        val orderRequest = this.findOrderRequest(orderRequestId, lineItemId, request)
        val lineItem = this.findLineItem(orderRequest, lineItemId, request)
        return this.saveLineItem(orderRequest, lineItem, request)
    }

}
