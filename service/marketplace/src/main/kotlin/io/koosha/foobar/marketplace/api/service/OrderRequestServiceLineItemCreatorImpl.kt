package io.koosha.foobar.marketplace.api.service

import feign.FeignException
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.openapitools.client.model.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator


@Service
class OrderRequestServiceLineItemCreatorImpl(
    private val validator: Validator,

    private val productClient: ProductApi,

    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
    private val lineItemFinder: OrderRequestServiceLineItemFinderImpl,
) {

    private val log = KotlinLogging.logger {}


    private fun validate(request: LineItemRequest) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("add lineItem validation error, errors={}", errors, kv("validationErrors", errors))
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }
    }

    private fun getOrderRequest(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestDO {

        val orderRequest: OrderRequestDO = this.finder.findByIdOrFail(orderRequestId)

        if (orderRequest.state != OrderRequestState.ACTIVE) {
            log.debug(
                "refused to add lineItem in current state of orderRequest, orderRequest={}, request={}",
                orderRequest,
                request,
                kv("orderRequest", orderRequest),
                kv("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
                msg = "order request is not active, can not add line item"
            )
        }

        return orderRequest
    }

    private fun getProduct(
        orderRequest: OrderRequestDO,
        request: LineItemRequest,
    ): Product {

        log.trace("fetching product, productId={}", request.productId, kv("productId", request.productId))
        val product = try {
            this.productClient.getProduct(request.productId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add lineItem, product not found, orderRequest={}, request={}",
                orderRequest,
                request,
                kv("orderRequest", orderRequest),
                kv("request", request),
            )
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
            log.debug(
                "refused to add lineItem in current state of product, orderRequest={}, product={}, request={}",
                orderRequest,
                product,
                request,
                kv("orderRequest", orderRequest),
                kv("product", product),
                kv("request", request),
            )
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
            .lineItemFinder
            .getLineItems(orderRequestId)
            .filter { it.productId == product.productId }
            .map { it.productId!! }

        if (existing.isNotEmpty()) {
            log.trace(
                "orderRequest already has a line item for productIds={}",
                existing,
                kv("productIds", existing),
            )
            throw EntityBadValueException(
                entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                entityId = null,
                "orderRequest already has a line item for productIds=${existing.joinToString(", ")}",
            )
        }
    }

    private fun saveLineItem(
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

        log.info(
            "adding order request line item, orderRequest={} lineItem={}",
            orderRequest,
            lineItem,
            kv("orderRequest", orderRequest),
            kv("lineItem", lineItem),
            kv("request", request),
        )
        val saved = this.lineItemRepo.save(lineItem)
        return saved
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): OrderRequestLineItemDO {

        this.validate(request)

        val orderRequest = this.getOrderRequest(orderRequestId, request)
        val product = this.getProduct(orderRequest, request)
        this.addLineItemValidateLineItems(orderRequestId, product)
        val lineItem = this.saveLineItem(orderRequest, request)

        return lineItem
    }

}
