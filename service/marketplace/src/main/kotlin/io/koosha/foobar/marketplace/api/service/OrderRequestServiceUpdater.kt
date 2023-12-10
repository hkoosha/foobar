package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.HeaderProto
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.marketplace.api.cfg.prop.ServicesProperties
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.dto.OrderRequestUpdateRequestDto
import io.koosha.foobar.marketplace.api.model.dto.SellerDto
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestRepository
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URI
import java.time.Clock
import java.util.UUID

@Service
class OrderRequestServiceUpdater(
    private val clock: Clock,
    private val validator: Validator,
    private val services: ServicesProperties,
    private val webClient: WebClient,
    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val finder: OrderRequestServiceFinding,

    @Qualifier(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    private val kafka: KafkaTemplate<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        log.info("Sending to topic: {}", this.kafka.defaultTopic)
    }

    private fun sellerGetApiAddr(
        sellerId: UUID,
    ): URI =
        URI.create(this.services.seller().address() + "/foobar/seller/v1/seller/$sellerId")

    private fun findSeller(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequestDto,
    ): Mono<SellerDto> {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))

        return this
            .webClient
            .get()
            .uri(this.sellerGetApiAddr(request.sellerId!!))
            .retrieve()
            .bodyToMono(SellerDto::class.java)
            .flatMap {
                if (!it.isActive) {
                    log.debug(
                        "refused to update orderRequest in current state of seller, " +
                                "orderRequest={}, request={}, seller={}",
                        v("orderRequest", orderRequest),
                        v("request", request),
                        v("seller", it),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = "seller",
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
                        entityType = "seller",
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
        request: OrderRequestUpdateRequestDto,
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
                    entityType = "order_request",
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
                    entityType = "order_request",
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
                    entityType = "order_request",
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
                    return@flatMap Mono.error<Pair<
                            OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged>>(
                        EntityInIllegalStateException(
                            entityType = "order_request",
                            entityId = orderRequest.orderRequestId,
                            msg = "order request is not live, can not update seller"
                        )
                    )
                }

                if (request.state != null && !this.isStateTransitionValid(orderRequest, request.state!!)) {
                    this.log.trace(
                        "update orderRequest validation error: invalid state transition, " +
                                "orderRequest={}, request={}",
                        v("orderRequest", orderRequest),
                        v("request", request),
                    )
                    return@flatMap Mono.error<
                            Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged>>(
                        EntityInIllegalStateException(
                            entityType = "order_request",
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
                                .setSource("marketplace")
                        )

                    val ret: Mono<Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged>> =
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
                    ret
                }
                else if (request.sellerId != null) {
                    val builder = OrderRequestStateChangedProto.OrderRequestStateChanged
                        .newBuilder()
                        .setFrom(orderRequest.state.toString())
                        .setTo(OrderRequestState.FULFILLED.toString())
                        .setHeader(
                            HeaderProto.Header.newBuilder()
                                .setTimestamp(this.clock.millis())
                                .setSource("marketplace")
                        )

                    val ret: Mono<Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged>> =
                        this.findSeller(orderRequest, request)
                            .map { seller ->
                                orderRequest.state = OrderRequestState.WAITING_FOR_SELLER
                                orderRequest.sellerId = seller.sellerId.toString()
                                orderRequest.subTotal = request.subTotal
                                orderRequest to builder.build()
                            }
                    ret
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
                            this.kafka
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
