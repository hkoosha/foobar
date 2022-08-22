package io.koosha.foobar.marketplace.api.service

import feign.FeignException
import io.koosha.foobar.HeaderProto
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestProcessQueueDO
import io.koosha.foobar.marketplace.api.model.OrderRequestProcessQueueRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import mu.KotlinLogging
import org.openapitools.client.model.Product
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
class OrderRequestServiceImpl(

    private val clock: Clock,
    private val validator: Validator,

    private val sellerClient: SellerApi,
    private val productClient: ProductApi,
    private val customerClient: CustomerApi,

    private val orderRequestRepo: OrderRequestRepository,
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val orderRequestProcessQueueStateChangeRepo: OrderRequestProcessQueueRepository,

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    private val kafka: KafkaTemplate<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,

    ) : OrderRequestService {

    companion object {
        private const val KAFKA_TIMEOUT_MILLIS = 3000L
    }

    private val log = KotlinLogging.logger {}

    private fun findOrderRequestOrFail(orderRequestId: UUID): OrderRequestDO =
        this.orderRequestRepo.findById(orderRequestId).orElseThrow {
            log.trace { "orderRequest not found, orderRequestId=$orderRequestId" }
            EntityNotFoundException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
            )
        }


    override fun findById(orderRequestId: UUID): Optional<OrderRequestDO> =
        this.orderRequestRepo.findById(orderRequestId)

    override fun findByIdOrFail(orderRequestId: UUID): OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)

    override fun findAll(): Iterable<OrderRequestDO> = this.orderRequestRepo.findAll()

    override fun findAllOrderRequestsOfCustomer(customerId: UUID): Iterable<OrderRequestDO> =
        this.orderRequestRepo.findAllByCustomerId(customerId)

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun create(request: OrderRequestCreateRequest): OrderRequestDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "create orderRequest validation error: $errors" }
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }

        log.trace { "fetching customer, customerId=${request.customerId}" }
        val customer = try {
            this.customerClient.getCustomer(request.customerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug { "refused to add orderRequest, customer not found, req=$request" }
            throw EntityNotFoundException(
                context = setOf(
                    EntityInfo(
                        entityType = CustomerApi.ENTITY_TYPE,
                        entityId = request.customerId,
                    ),
                ),
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching customer", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }

        if (!customer.isActive) {
            log.debug {
                "refused to create order request in current state of customer, customer=$customer, request=$request"
            }
            throw EntityInIllegalStateException(
                entityType = CustomerApi.ENTITY_TYPE,
                entityId = request.customerId,
                msg = "customer is not active",
            )
        }

        val orderRequest = OrderRequestDO()
        orderRequest.orderRequestId = UUID.randomUUID()
        orderRequest.customerId = request.customerId
        orderRequest.lineItemIdPool = 0
        orderRequest.state = OrderRequestState.ACTIVE

        log.info { "creating new orderRequest, orderRequest=$orderRequest" }
        this.orderRequestRepo.save(orderRequest)
        return orderRequest
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun delete(orderRequestId: UUID) {

        val maybeEntity: Optional<OrderRequestDO> = this.findById(orderRequestId)
        if (!maybeEntity.isPresent) {
            log.debug { "not deleting order request, entity does not exist, orderRequestId=$orderRequestId" }
            return
        }

        val orderRequest: OrderRequestDO = maybeEntity.get()

        if (orderRequest.state?.deletionAllowed != true) {
            log.debug { "refused to delete orderRequest in current state, orderRequest=$orderRequest" }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request can not be deleted in its current state"
            )
        }

        log.info { "deleting orderRequest and lineItems, orderRequest=$orderRequest" }
        this.lineItemRepo.deleteAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequestId)
        this.orderRequestRepo.delete(orderRequest)
    }

    // =========================================================================

    private fun setSellerCheckSubTotal(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ) {

        if (request.subTotal == null) {
            log.trace {
                "update orderRequest validation error: subTotal not set, orderRequest=$orderRequest, request=$request"
            }
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "subTotal not set",
            )
        }
    }

    private fun setSellerCheckState(
        orderRequest: OrderRequestDO,
    ) {

        if (orderRequest.state != OrderRequestState.LIVE) {
            log.debug { "refused to update orderRequest in current state, orderRequest=$orderRequest" }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "order request is not live, can not update seller"
            )
        }
    }

    private fun setSellerFindSeller(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ): Seller {

        log.trace { "fetching seller, sellerId=${request.sellerId}" }

        val seller = try {
            this.sellerClient.getSeller(request.sellerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug { "refused to update orderRequest, seller not found, orderRequest=$orderRequest, req=$request" }
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
            log.debug {
                "refused to update orderRequest in current state of seller," +
                        " orderRequest=$orderRequest, request=$request, seller=$seller"
            }
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

        this.setSellerCheckSubTotal(orderRequest, request)
        this.setSellerCheckState(orderRequest)

        val seller = this.setSellerFindSeller(orderRequest, request)

        orderRequest.state = OrderRequestState.WAITING_FOR_SELLER
        orderRequest.sellerId = seller.sellerId
        orderRequest.subTotal = request.subTotal
    }

    private fun setState(
        orderRequest: OrderRequestDO,
        request: OrderRequestUpdateRequest,
    ): OrderRequestStateChangedProto.OrderRequestStateChanged {

        if (!isStateTransitionValid(orderRequest, request.state!!)) {
            log.trace {
                "update orderRequest validation error: invalid state transition, " +
                        "orderRequest=$orderRequest, request=$request"
            }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequest.orderRequestId,
                msg = "can not set state from=${orderRequest.state} to=${request.state}"
            )
        }

        log.debug {
            "setting order request state, " +
                    "orderRequestId=${orderRequest.orderRequestId} " +
                    "oldState=${orderRequest.state} newState=${request.state}"
        }

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

    internal fun updateDb(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): Pair<OrderRequestDO, OrderRequestStateChangedProto.OrderRequestStateChanged?> {

        if (request.sellerId != null && request.state != null) {
            log.trace { "update orderRequest validation error: can not set both sellerId and state at the same time" }
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "can not set both sellerId and state at the same time"
            )
        }

        val orderRequest: OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)

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

        log.info { "updating orderRequest, orderRequest=$orderRequest" }
        this.orderRequestRepo.save(orderRequest)
        this.orderRequestProcessQueueStateChangeRepo.save(queueStateChange)

        return orderRequest to stateChange
    }

    internal fun updateKafka(
        orderRequestId: UUID,
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
    ) {

        val orderRequest: OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)
        val queueStateChange =
            this.orderRequestProcessQueueStateChangeRepo.findById(orderRequest.orderRequestId!!).orElseThrow {
                IllegalStateException("missing queueStateChange for orderRequest=$orderRequest")
            }
        queueStateChange.synced = true

        log.trace {
            "sending new orderRequest state to kafka, orderRequest=$orderRequest"
        }
        this.kafka
            .sendDefault(orderRequestId, stateChange)
            .get(KAFKA_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)

        this.orderRequestProcessQueueStateChangeRepo.save(queueStateChange)
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun update(
        orderRequestId: UUID,
        request: OrderRequestUpdateRequest,
    ): OrderRequestDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "update orderRequest validation error: $errors" }
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


    // =========================================================================

    private fun addLineItemValidateArgs(request: LineItemRequest) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "add lineItem validation error: $errors" }
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }
    }

    private fun addLineItemGetOrderRequest(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestDO {

        val orderRequest: OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)

        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug {
                "refused to add lineItem in current state of orderRequest, orderRequest=$orderRequest, request=$request"
            }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not add line item"
            )
        }

        return orderRequest
    }

    private fun addLineItemGetProduct(
        orderRequest: OrderRequestDO,
        request: LineItemRequest,
    ): Product {

        log.trace { "fetching product, productId=${request.productId}" }

        val product = try {
            this.productClient.getProduct(request.productId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug { "refused to add lineItem, product not found, orderRequest=$orderRequest, req=$request" }
            throw EntityNotFoundException(
                entityType = ProductApi.ENTITY_TYPE,
                entityId = request.productId,
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching seller", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }

        if (!product.active) {
            log.debug {
                "refused to add lineItem in current state of product, " +
                        "orderRequest=$orderRequest, product=$product, request=$request"
            }
            throw EntityInIllegalStateException(
                entityType = ProductApi.ENTITY_TYPE,
                request.productId,
                msg = "product is not active, can not add line item",
            )
        }

        return product
    }

    private fun addLineItemValidateLineItems(
        orderRequestId: UUID,
        product: Product,
    ) {

        val existing: List<UUID> = this
            .getLineItems(orderRequestId)
            .filter { it.productId == product.productId }
            .map { it.productId!! }

        if (existing.isNotEmpty()) {
            log.trace { "orderRequest already has a line item for productIds=${existing.joinToString(", ")}" }
            throw EntityBadValueException(
                entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                entityId = null,
                "orderRequest already has a line item for productIds=${existing.joinToString(", ")}",
            )
        }
    }

    private fun addLineItemSaveLineItem(
        orderRequest: OrderRequestDO,
        request: LineItemRequest,
    ): OrderRequestLineItemDO {

        val lineItemId = orderRequest.lineItemIdPool!! + 1
        orderRequest.lineItemIdPool = lineItemId

        val lineItem = OrderRequestLineItemDO()
        lineItem.orderRequestLineItemPk.orderRequestLineItemId = lineItemId
        lineItem.orderRequestLineItemPk.orderRequest = orderRequest
        lineItem.units = request.units
        lineItem.productId = request.productId

        val existingLineItem = this.lineItemRepo.findById(
            OrderRequestLineItemDO.Pk(
                lineItemId,
                orderRequest,
            )
        )
        check(!existingLineItem.isPresent) {
            "duplicate lineItem=${existingLineItem.get().orderRequestLineItemPk}"
        }

        log.info { "adding order request line item, orderRequestId=${orderRequest.orderRequestId} lineItem=$lineItem" }
        val saved = this.lineItemRepo.save(lineItem)

        return saved
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestLineItemDO {

        this.addLineItemValidateArgs(request)

        val orderRequest = this.addLineItemGetOrderRequest(orderRequestId, request)
        val product = this.addLineItemGetProduct(orderRequest, request)
        this.addLineItemValidateLineItems(orderRequestId, product)
        val lineItem = this.addLineItemSaveLineItem(orderRequest, request)

        return lineItem
    }

    // =========================================================================

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun deleteLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ) {

        val orderRequest: OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)

        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug {
                "refused to delete lineItem in current state of orderRequest, " +
                        "orderRequest=$orderRequest, lineItemId=$lineItemId"
            }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not delete line item",
            )
        }

        val lineItem = this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest))

        if (lineItem.isPresent) {
            log.info { "removing line item, orderRequestId=${orderRequest.orderRequestId} lineItemId=$lineItemId" }
            this.lineItemRepo.delete(lineItem.get())
        }
        else {
            log.debug {
                "not removing line item as it is not present, " +
                        "orderRequestId=${orderRequest.orderRequestId} lineItemId=$lineItemId"
            }
        }
    }

    @Transactional(readOnly = true)
    override fun getLineItems(orderRequestId: UUID): Iterable<OrderRequestLineItemDO> {

        this.findOrderRequestOrFail(orderRequestId)
        val all = this.lineItemRepo.findAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequestId)
        return all
    }

    @Transactional(readOnly = true)
    override fun getLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
    ): OrderRequestLineItemDO {

        val orderRequest = this.findOrderRequestOrFail(orderRequestId)
        val lineItem = this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest)).orElseThrow {
            log.trace { "lineItem not found, orderRequestId=$orderRequestId, lineItemId=$lineItemId" }
            EntityNotFoundException(
                entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                entityId = lineItemId,
            )
        }
        return lineItem
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun updateLineItem(
        orderRequestId: UUID,
        lineItemId: Long,
        request: LineItemUpdateRequest,
    ): OrderRequestLineItemDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "update lineItem validation error: $errors" }
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }

        val orderRequest: OrderRequestDO = this.findOrderRequestOrFail(orderRequestId)
        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug {
                "refused to update lineItem in current state of orderRequest, " +
                        "orderRequest=$orderRequest, lineItemId=$lineItemId, request=$request"
            }
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not modify line item",
            )
        }

        val lineItem: OrderRequestLineItemDO =
            this.lineItemRepo.findById(OrderRequestLineItemDO.Pk(lineItemId, orderRequest)).orElseThrow {
                log.trace { "lineItem not found, orderRequestId=$orderRequestId, lineItemId=$lineItemId" }
                EntityNotFoundException(
                    entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                    entityId = lineItemId,
                )
            }

        var anyChange = false

        if (request.units != null) {
            lineItem.units = request.units
            anyChange = true
        }

        if (anyChange) {
            log.info {
                "updating order request line item, " +
                        "orderRequestId=${orderRequest.orderRequestId}, " +
                        "lineItemId=${lineItem.orderRequestLineItemPk.orderRequestLineItemId}, " +
                        "request=$request"
            }

            this.lineItemRepo.save(lineItem)
        }

        return lineItem
    }


    private fun isStateTransitionValid(
        orderRequest: OrderRequestDO,
        target: OrderRequestState,
    ): Boolean {
        return when (target) {
            OrderRequestState.ACTIVE -> false
            OrderRequestState.LIVE -> orderRequest.state == OrderRequestState.ACTIVE
            OrderRequestState.WAITING_FOR_SELLER -> orderRequest.state == OrderRequestState.LIVE
            OrderRequestState.FULFILLED -> orderRequest.state == OrderRequestState.WAITING_FOR_SELLER
            OrderRequestState.NO_SELLER_FOUND -> orderRequest.state == OrderRequestState.LIVE
        }
    }

}
