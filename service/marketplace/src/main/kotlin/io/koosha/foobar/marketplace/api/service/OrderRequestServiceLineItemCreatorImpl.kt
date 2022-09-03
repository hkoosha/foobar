package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.connect.warehouse.rx.generated.api.Product
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Validator


// TODO handle and translate retryable errors.
@Service
class OrderRequestServiceLineItemCreatorImpl(
    private val validator: Validator,

    private val productClient: ProductApi,

    private val lineItemRepo: OrderRequestLineItemRepository,

    private val finder: OrderRequestServiceFindingImpl,
    private val lineItemFinder: OrderRequestServiceLineItemFinderImpl,
) {

    companion object {
        const val MAX_LINE_ITEMS = 100
    }

    private val log = KotlinLogging.logger {}


    private fun getOrderRequest(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): Mono<OrderRequestDO> =
        this.finder
            .findByIdOrFail(orderRequestId)
            .flatMap { orderRequest ->
                if (orderRequest.state != OrderRequestState.ACTIVE) {
                    log.debug(
                        "refused to add lineItem in current state of orderRequest, orderRequest={}, request={}",
                        v("orderRequest", orderRequest),
                        v("request", request),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequestId,
                            msg = "order request is not active, can not add line item"
                        )
                    )
                }
                else {
                    Mono.just(orderRequest)
                }
            }

    private fun getProduct(
        orderRequest: OrderRequestDO,
        request: LineItemRequest,
    ): Mono<Product> {

        log.trace("fetching product, productId={}", v("productId", request.productId))
        return this
            .productClient
            .getProduct(request.productId)
            .flatMap {
                if (it.active) {
                    Mono.just(it)
                }
                else {
                    log.debug(
                        "refused to add lineItem in current state of product, orderRequest={}, product={}, request={}",
                        v("orderRequest", orderRequest),
                        v("product", it),
                        v("request", request),
                    )
                    Mono.error(
                        EntityInIllegalStateException(
                            entityType = ENTITY_TYPE__PRODUCT,
                            request.productId,
                            msg = "product is not active, can not add line item",
                        )
                    )
                }
            }
            .onErrorMap {
                if (it is WebClientResponseException.NotFound)
                    EntityNotFoundException(
                        entityType = ENTITY_TYPE__PRODUCT,
                        entityId = request.productId,
                    )
                else
                    it
            }
    }

    private fun validateLineItems(
        orderRequestId: UUID,
        product: Product,
    ): Mono<String> =
        this.lineItemFinder
            .getLineItems(orderRequestId)
            .filter { it.productId == product.productId.toString() }
            .map { it.productId!! }
            .collectList()
            .flatMap { existing ->
                if (existing.isEmpty()) {
                    Mono.just("")
                }
                else if (existing.size > MAX_LINE_ITEMS) {
                    log.trace("orderRequest already has too many line items: {}", existing.size)
                    Mono.error(
                        EntityBadValueException(
                            entityType = OrderRequestDO.ENTITY_TYPE,
                            entityId = orderRequestId,
                            "orderRequest=$orderRequestId already has too many line items",
                        )
                    )
                }
                else {
                    val ids = existing.joinToString(", ")
                    log.trace("orderRequest already has a line item for productIds={}", v("productIds", ids))
                    Mono.error(
                        EntityBadValueException(
                            entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                            entityId = null,
                            "orderRequest=$orderRequestId already has line item for productIds=$ids",
                        )
                    )
                }
            }

    private fun saveLineItem(
        orderRequest: OrderRequestDO,
        request: LineItemRequest,
    ): Mono<OrderRequestLineItemDO> {

        val lineItemId = orderRequest.lineItemIdPool!! + 1
        orderRequest.lineItemIdPool = lineItemId

        val lineItem = OrderRequestLineItemDO()
        lineItem.internalOrderRequestLineItemId = UUID.randomUUID().toString()
        lineItem.orderRequestLineItemId = lineItemId
        lineItem.orderRequestId = orderRequest.orderRequestId
        lineItem.units = request.units
        lineItem.productId = request.productId.toString()

        log.info(
            "adding order request line item, orderRequest={} lineItem={}, request={}",
            v("orderRequest", orderRequest),
            v("lineItem", lineItem),
            v("request", request),
        )

        return lineItemRepo
            .save(lineItem)
            .doFinally {
                log.info(
                    "order request line item added, orderRequest={} lineItem={}, request={}",
                    v("orderRequest", orderRequest),
                    v("lineItem", lineItem),
                    v("request", request),
                )
            }
    }

    fun addLineItem(
        orderRequestId: UUID,
        request: LineItemRequest,
    ): Mono<OrderRequestLineItemDO> {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("add lineItem validation error, errors={}", v("validationErrors", errors))
            return Mono.error(
                EntityBadValueException(
                    entityType = OrderRequestDO.ENTITY_TYPE,
                    entityId = null,
                    errors,
                )
            )
        }

        return this
            .getOrderRequest(orderRequestId, request)
            .flatMap { orderRequest ->
                this.getProduct(orderRequest, request)
                    .flatMap { product ->
                        this.validateLineItems(orderRequestId, product)
                            .map {
                                orderRequest
                            }
                    }
            }
            .flatMap {
                this.saveLineItem(it, request)
            }
    }

}
