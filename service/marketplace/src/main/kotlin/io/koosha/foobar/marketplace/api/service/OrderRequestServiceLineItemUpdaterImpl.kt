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
import reactor.core.publisher.Mono
import java.util.*
import jakarta.validation.Validator


@Service
class OrderRequestServiceLineItemUpdaterImpl(
    private val validator: Validator,

    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}


    fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): Mono<OrderRequestLineItemDO> {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update lineItem validation error, orderRequestId={} lineItemId={} errors={}",
                v("orderRequestId", orderRequestId),
                v("lineItemId", lineItemId),
                v("validationErrors", errors),
            )
            return Mono.error(
                EntityBadValueException(
                    entityType = OrderRequestDO.ENTITY_TYPE,
                    entityId = null,
                    errors,
                )
            )
        }

        val orderRequestMono: Mono<OrderRequestDO> = this
            .finder
            .findByIdOrFail(orderRequestId)
            .flatMap { orderRequest ->
                if (orderRequest.state != OrderRequestState.ACTIVE) {
                    log.debug(
                        "refused to update lineItem in current state of orderRequest, " +
                                "orderRequest={}, lineItemId={}, request={}",
                        v("orderRequest", orderRequest),
                        v("lineItemId", lineItemId),
                        v("request", request),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequestId,
                            msg = "order request is not active, can not modify line item",
                        )
                    )
                }
                else {
                    Mono.just(orderRequest)
                }
            }

        val lineItemMono: Mono<OrderRequestLineItemDO> = this
            .lineItemRepo
            .findByOrderRequestIdAndOrderRequestLineItemId(orderRequestId.toString(), lineItemId)
            .switchIfEmpty(Mono.defer {
                log.trace(
                    "lineItem not found, orderRequest={}, lineItemId={}, request={}",
                    v("orderRequestId", orderRequestId),
                    v("lineItemId", lineItemId),
                    v("request", request),
                )
                Mono.error(
                    EntityNotFoundException(
                        entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                        entityId = lineItemId,
                    )
                )
            })

        return Mono
            .zip(orderRequestMono, lineItemMono)
            .flatMap {
                val orderRequest: OrderRequestDO = it.t1
                val lineItem: OrderRequestLineItemDO = it.t2
                if (request.units != null) {
                    lineItem.units = request.units
                    log.info(
                        "updating order request line item, orderRequest={}, lineItem={}, request={}",
                        v("orderRequest", orderRequest),
                        v("lineItem", lineItem),
                        v("request", request),
                    )
                    this.lineItemRepo.save(lineItem)
                }
                else {
                    Mono.just(lineItem)
                }
            }
    }

}
