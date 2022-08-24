package io.koosha.foobar.marketplace.api.service

import feign.FeignException
import io.koosha.foobar.HeaderProto
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestProcessQueueDO
import io.koosha.foobar.marketplace.api.model.OrderRequestProcessQueueRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.openapitools.client.model.Seller
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.*
import java.util.concurrent.TimeUnit
import javax.validation.Validator


@Service
class OrderRequestServiceUpdaterImpl(
    private val clock: Clock,
    private val validator: Validator,

    private val sellerClient: SellerApi,

    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val orderRequestProcessQueueStateChangeRepo: OrderRequestProcessQueueRepository,

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    private val kafka: KafkaTemplate<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,

    private val finder: OrderRequestServiceFindingImpl,
) {

    companion object {
        private const val KAFKA_TIMEOUT_MILLIS = 3000L
    }


    private val log = KotlinLogging.logger {}


    private fun checkSubTotal(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ) {

        if (request.subTotal == null) {
            log.trace(
                "update orderRequest validation error: subTotal not set, orderRequest={}, request={}",
                v("orderRequest", orderRequest),
                v("request", request),
            )
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "subTotal not set",
            )
        }
    }

    private fun checkState(
        orderRequest: OrderRequestDO,
    ) {

        if (orderRequest.state != OrderRequestState.LIVE) {
            log.debug(
                "refused to update orderRequest in current state, orderRequest={}",
                v("orderRequest", orderRequest),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "order request is not live, can not update seller"
            )
        }
    }

    private fun findSeller(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ): Seller {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))
        val seller = try {
            this.sellerClient.getSeller(request.sellerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to update orderRequest, seller not found, orderRequest={}, request={}",
                v("orderRequest", orderRequest),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = SellerApi.ENTITY_TYPE,
                entityId = request.sellerId,
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching seller", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }

        if (!seller.isActive) {
            log.debug(
                "refused to update orderRequest in current state of seller, orderRequest={}, request={}, seller={}",
                v("orderRequest", orderRequest),
                v("request", request),
                v("seller", seller),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "seller is not active, can not update order request"
            )
        }

        return seller
    }

    private fun setSeller(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ) {

        this.checkSubTotal(orderRequest, request)
        this.checkState(orderRequest)

        val seller = this.findSeller(orderRequest, request)

        orderRequest.state = OrderRequestState.WAITING_FOR_SELLER
        orderRequest.sellerId = seller.sellerId
        orderRequest.subTotal = request.subTotal
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

    private fun setState(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ): OrderRequestStateChangedProto.OrderRequestStateChanged {

        if (!isStateTransitionValid(orderRequest, request.state!!)) {
            log.trace(
                "update orderRequest validation error: invalid state transition, orderRequest={}, request={}",
                v("orderRequest", orderRequest),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "can not set state from=${orderRequest.state} to=${request.state}"
            )
        }

        log.debug(
            "setting order request state, orderRequest={} oldState={} newState={}",
            v("orderRequest", orderRequest),
            v("oldState", orderRequest.state),
            v("newState", request.state),
        )

        orderRequest.state = request.state

        val lineItems =
            this.lineItemRepo.findAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequest.orderRequestId!!)

        val builder = OrderRequestStateChangedProto.OrderRequestStateChanged.newBuilder()
            .setFrom(orderRequest.state.toString())
            .setTo(request.state.toString())
            .setHeader(
                HeaderProto.Header.newBuilder()
                    .setTimestamp(this.clock.millis())
                    .setSource(SOURCE)
            )
            .addAllLineItems(
                lineItems.map {
                    OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem
                        .newBuilder()
                        .setProductId(it.productId.toString())
                        .setUnits(it.units!!)
                        .build()
                }
            )

        return builder.build()
    }

    private fun updateDb(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged?> {

        if (request.sellerId != null && request.state != null) {
            log.trace(
                "update orderRequest validation error: can not set both sellerId and state at the same time," +
                        " orderRequestId={}, request={}",
                v("orderRequestId", orderRequestId),
                v("request", request),
            )
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "can not set both sellerId and state at the same time"
            )
        }

        val orderRequest: OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)
        val originalOrderRequest = orderRequest.detachedCopy()

        var anyChange = false

        if (request.sellerId != null) {
            this.setSeller(orderRequest, request)
            anyChange = true
        }

        val stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged? =
            if (request.state != null)
                this.setState(orderRequest, request)
            else
                null
        if (stateChange != null)
            anyChange = true

        if (!anyChange)
            return orderRequest to null

        val queueStateChange =
            orderRequestProcessQueueStateChangeRepo.findById(orderRequest.orderRequestId!!).orElseGet {
                OrderRequestProcessQueueDO(
                    orderRequest.orderRequestId,
                    orderRequest,
                    null,
                    null,
                    null,
                    false,
                )
            }

        queueStateChange.synced = false

        log.info(
            "updating orderRequest, orderRequest={}, request={}",
            v("orderRequest", originalOrderRequest),
            v("request", request),
        )
        this.orderRequestRepo.save(orderRequest)
        this.orderRequestProcessQueueStateChangeRepo.save(queueStateChange)

        return orderRequest to stateChange
    }

    private fun updateKafka(
        orderRequestId: UUID,
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
    ) {

        val orderRequest: OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)
        val queueStateChange =
            this.orderRequestProcessQueueStateChangeRepo.findById(orderRequest.orderRequestId!!).orElseThrow {
                IllegalStateException("missing queueStateChange for orderRequest=$orderRequest")
            }
        queueStateChange.synced = true

        log.trace("sending new orderRequest state to kafka, orderRequest={}", v("orderRequest", orderRequest))
        this.kafka
            .sendDefault(orderRequestId, stateChange)
            .get(KAFKA_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)

        this.orderRequestProcessQueueStateChangeRepo.save(queueStateChange)
    }


    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): OrderRequestDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update orderRequest validation error, orderRequestId={} request={} errors={}",
                v("orderRequestId", orderRequestId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }

        val (orderRequest, stateChange) = this.updateDb(orderRequestId, request)

        if (stateChange != null)
            this.updateKafka(orderRequestId, stateChange)

        return orderRequest
    }

}
