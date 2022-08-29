package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.HeaderProto
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.connect.seller.rx.generated.api.Seller
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.Clock
import java.util.*
import javax.validation.Validator


@Service
class OrderRequestServiceUpdaterImpl(
    private val clock: Clock,
    private val validator: Validator,

    private val sellerClient: SellerApi,

    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,
    // private val orderRequestProcessQueueStateChangeRepo: OrderRequestProcessQueueRepository,

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    private val kafka: KafkaTemplate<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,

    private val finder: OrderRequestServiceFindingImpl,
) {

    private val log = KotlinLogging.logger {}

    private fun findSeller(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ): Mono<Seller> {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))
        return this
            .sellerClient
            .getSeller(request.sellerId!!)
            .flatMap {
                if (!it.isActive) {
                    log.debug(
                        "refused to update orderRequest in current state of seller, orderRequest={}, request={}, seller={}",
                        v("orderRequest", orderRequest),
                        v("request", request),
                        v("seller", it),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = ENTITY_TYPE__SELLER,
                            entityId = orderRequest.orderRequestId,
                            msg = "seller is not active, can not update order request"
                        )
                    )
                }
                else {
                    Mono.just(it)
                }
            }
            .onErrorMap {
                if (it is WebClientResponseException.NotFound)
                    EntityNotFoundException(
                        entityType = ENTITY_TYPE__SELLER,
                        entityId = request.sellerId,
                    )
                else
                    it
            }
    }

    private fun isStateTransitionValid(
        orderRequest: OrderRequestDO,
        target: OrderRequestState,
    ): Boolean = when (target) {
        OrderRequestState.ACTIVE -> false
        OrderRequestState.LIVE -> orderRequest.state == OrderRequestState.ACTIVE
        OrderRequestState.WAITING_FOR_SELLER -> orderRequest.state == OrderRequestState.LIVE
        OrderRequestState.FULFILLED -> orderRequest.state == OrderRequestState.WAITING_FOR_SELLER
        OrderRequestState.NO_SELLER_FOUND -> orderRequest.state == OrderRequestState.LIVE
    }

    fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): Mono<OrderRequestDO> {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update orderRequest validation error, orderRequestId={} request={} errors={}",
                v("orderRequestId", orderRequestId),
                v("request", request),
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

        if (request.sellerId != null && request.state != null) {
            log.trace(
                "update orderRequest validation error: can not set both sellerId and state at the same time," +
                        " orderRequestId={}, request={}",
                v("orderRequestId", orderRequestId),
                v("request", request),
            )
            return Mono.error(
                EntityBadValueException(
                    entityType = OrderRequestDO.ENTITY_TYPE,
                    entityId = orderRequestId,
                    msg = "can not set both sellerId and state at the same time"
                )
            )
        }

        if (request.sellerId != null && request.subTotal == null) {
            log.trace(
                "update orderRequest validation error: subTotal not set, orderRequest={}, request={}",
                v("orderRequestId", orderRequestId),
                v("request", request),
            )
            return Mono.error(
                EntityBadValueException(
                    entityType = OrderRequestDO.ENTITY_TYPE,
                    entityId = orderRequestId,
                    msg = "subTotal not set",
                )
            )
        }

        return this
            .finder
            .findByIdOrFail(orderRequestId)
            .flatMap { orderRequest ->

                if (request.sellerId != null && orderRequest.state != OrderRequestState.LIVE) {
                    this.log.debug(
                        "refused to update orderRequest in current state, orderRequest={}",
                        v("orderRequest", orderRequest),
                    )
                    return@flatMap Mono.error<Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged?>?>(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequest.orderRequestId,
                            msg = "order request is not live, can not update seller"
                        )
                    )
                }

                if (request.state != null && !this.isStateTransitionValid(orderRequest, request.state!!)) {
                    this.log.trace(
                        "update orderRequest validation error: invalid state transition, orderRequest={}, request={}",
                        v("orderRequest", orderRequest),
                        v("request", request),
                    )
                    return@flatMap Mono.error<Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged?>?>(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequest.orderRequestId,
                            msg = "can not set state from=${orderRequest.state} to=${request.state}"
                        )
                    )
                }

                val originalOrderRequest = orderRequest.detachedCopy()

                if (request.state != null) {
                    this.log.debug(
                        "setting order request state, orderRequest={} oldState={} newState={}",
                        v("orderRequest", orderRequest),
                        v("oldState", orderRequest.state),
                        v("newState", request.state),
                    )

                    orderRequest.state = request.state

                    val builder = OrderRequestStateChangedProto.OrderRequestStateChanged
                        .newBuilder()
                        .setFrom(orderRequest.state.toString())
                        .setTo(request.state.toString())
                        .setHeader(
                            HeaderProto.Header.newBuilder()
                                .setTimestamp(this.clock.millis())
                                .setSource(SOURCE)
                        )

                    this.lineItemRepo
                        .findAllByOrderRequestId(orderRequest.orderRequestId!!)
                        .map {
                            builder.addLineItems(
                                OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem
                                    .newBuilder()
                                    .setProductId(it.productId.toString())
                                    .setUnits(it.units!!)
                                    .build()
                            )
                        }
                        .last()
                        .map {
                            this.log.info(
                                "updating orderRequest, orderRequest={}, request={}",
                                v("orderRequest", originalOrderRequest),
                                v("request", request),
                            )
                            orderRequest to it.build()
                        }
                }
                else if (request.sellerId != null) {
                    val builder = OrderRequestStateChangedProto.OrderRequestStateChanged
                        .newBuilder()
                        .setFrom(orderRequest.state.toString())
                        .setTo(OrderRequestState.FULFILLED.toString())
                        .setHeader(
                            HeaderProto.Header.newBuilder()
                                .setTimestamp(this.clock.millis())
                                .setSource(SOURCE)
                        )

                    this.findSeller(orderRequest, request)
                        .map { seller ->
                            orderRequest.state = OrderRequestState.WAITING_FOR_SELLER
                            orderRequest.sellerId = seller.sellerId.toString()
                            orderRequest.subTotal = request.subTotal
                            orderRequest to builder.build()
                        }
                }
                else {
                    Mono.just(orderRequest to null)
                }
            }
            .flatMap { it0 ->
                val orderRequest = it0.first
                val changed = it0.second
                if (changed != null) {
                    this.orderRequestRepo
                        .save(orderRequest)
                        .flatMap {
                            log.trace(
                                "sending new orderRequest state to kafka, orderRequest={}",
                                v("orderRequest", orderRequest)
                            )
                            kafka
                                .sendDefault(orderRequest.orderRequestId!!.toUUID(), changed)
                                .toMono()
                                .map {
                                    orderRequest
                                }
                        }
                }
                else {
                    Mono.just(orderRequest)
                }
            }
    }

}
