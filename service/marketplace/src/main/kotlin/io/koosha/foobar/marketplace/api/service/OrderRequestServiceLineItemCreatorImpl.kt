package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.connect.warehouse.rx.generated.api.Product
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
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
    }

    private fun validateLineItems(
        orderRequestId: UUID,
        product: Product,
    ): Mono<String> =
        this.lineItemFinder
            .getLineItems(orderRequestId)
            .filter { it.productId == product.productId.toString() }
            .map { it.productId!! }
            .last("")
            .flatMap { existing ->
                if (existing.isEmpty()) {
                    Mono.just("")
                }
                else {
                    // TODO how to get all UUIDs?
                    log.trace("orderRequest already has a line item for productIds={}", v("productIds", existing))
                    Mono.error(
                        EntityBadValueException(
                            entityType = OrderRequestLineItemDO.ENTITY_TYPE,
                            entityId = null,
                            "orderRequest already has a line item for productId=$existing",
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

        // val existingLineItem = this.lineItemRepo.findById(
        //     OrderRequestLineItemDO.Pk(
        //         lineItemId,
        //         orderRequest,
        //     )
        // )
        // check(!existingLineItem.isPresent) {
        //     "duplicate lineItem=${existingLineItem.get().orderRequestLineItemPk}"
        // }

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
