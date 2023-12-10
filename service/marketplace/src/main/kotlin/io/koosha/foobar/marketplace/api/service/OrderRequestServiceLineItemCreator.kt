package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.cfg.prop.ServicesProperties
import io.koosha.foobar.marketplace.api.model.dto.LineItemRequestDto
import io.koosha.foobar.marketplace.api.model.dto.ProductDto
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestLineItemDO
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestLineItemRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Clock
import java.util.UUID

// TODO handle and translate retryable errors.
@Service
class OrderRequestServiceLineItemCreator(
    private val validator: Validator,
    private val services: ServicesProperties,
    private val clock: Clock,
    private val webClient: WebClient,
    private val lineItemRepo: OrderRequestLineItemRepository,
    private val finder: OrderRequestServiceFinding,
    private val lineItemFinder: OrderRequestServiceLineItemFinder,
) {

    companion object {
        const val MAX_LINE_ITEMS = 100
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun warehouseGetApiAddr(
        productId: UUID,
    ): URI =
        URI.create(this.services.warehouse().address() + "/foobar/warehouse/v1/product/$productId")

    private fun getOrderRequest(
        orderRequestId: UUID,
        request: LineItemRequestDto,
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
                            entityType = "order_request",
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
        request: LineItemRequestDto,
    ): Mono<ProductDto> {

        log.trace("fetching product, productId={}", v("productId", request.productId))

        return this
            .webClient
            .get()
            .uri(this.warehouseGetApiAddr(request.productId!!))
            .retrieve()
            .bodyToMono(ProductDto::class.java)
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
                            entityType = "product",
                            request.productId,
                            msg = "product is not active, can not add line item",
                        )
                    )
                }
            }
            .onErrorMap {
                if (it is WebClientResponseException.NotFound)
                    EntityNotFoundException(
                        entityType = "product",
                        entityId = request.productId,
                    )
                else
                    it
            }
    }

    private fun validateLineItems(
        orderRequestId: UUID,
        product: ProductDto,
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
                            entityType = "order_request",
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
                            entityType = "order_request_line_item",
                            entityId = null,
                            "orderRequest=$orderRequestId already has line item for productIds=$ids",
                        )
                    )
                }
            }

    private fun saveLineItem(
        orderRequest: OrderRequestDO,
        request: LineItemRequestDto,
    ): Mono<OrderRequestLineItemDO> {

        val lineItemId = orderRequest.lineItemIdPool!! + 1
        orderRequest.lineItemIdPool = lineItemId

        val lineItem = OrderRequestLineItemDO()
        lineItem.created = this.clock.instant()
        lineItem.updated = lineItem.created
        lineItem.internalOrderRequestLineItemId = UUID.randomUUID().toString()
        lineItem.orderRequestLineItemId = lineItemId
        lineItem.orderRequestId = orderRequest.orderRequestId
        lineItem.units = request.units
        lineItem.productId = request.productId.toString()

        log.trace(
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
        request: LineItemRequestDto,
    ): Mono<OrderRequestLineItemDO> {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("add lineItem validation error, errors={}", v("validationErrors", errors))
            return Mono.error(
                EntityBadValueException(
                    entityType = "order_request",
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
