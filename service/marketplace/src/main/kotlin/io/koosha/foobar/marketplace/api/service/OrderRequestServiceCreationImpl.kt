package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.connect.customer.rx.generated.api.Customer
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.util.*
import jakarta.validation.Validator


@Service
class OrderRequestServiceCreationImpl(
    private val validator: Validator,

    private val customerClient: CustomerApi,

    private val orderRequestRepo: OrderRequestRepository,
) {

    private val log = KotlinLogging.logger {}


    private fun findCustomer(
        request: OrderRequestCreateRequest,
    ): Mono<Customer> {

        log.trace("fetching customer, customerId={}", v("customerId", request.customerId))
        return this
            .customerClient
            .getCustomer(request.customerId!!)
            .flatMap {
                if (it.isActive) {
                    Mono.just(it)
                }
                else {
                    log.debug("refused to add orderRequest, customer not found, request={}", v("request", request))
                    Mono.error(
                        EntityNotFoundException(
                            context = setOf(
                                EntityInfo(
                                    entityType = ENTITY_TYPE__CUSTOMER,
                                    entityId = request.customerId,
                                ),
                            ),
                        )
                    )
                }
            }
            .onErrorMap {
                if (it is WebClientResponseException.NotFound)
                    EntityNotFoundException(
                        entityType = ENTITY_TYPE__SELLER,
                        entityId = request.customerId,
                    )
                else
                    it
            }
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun create(
        request: OrderRequestCreateRequest,
    ): Mono<OrderRequestDO> {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create orderRequest validation error, request={} errors={}",
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

        return this
            .findCustomer(request)
            .flatMap {
                val orderRequest = OrderRequestDO()
                orderRequest.orderRequestId = UUID.randomUUID().toString()
                orderRequest.customerId = request.customerId.toString()
                orderRequest.lineItemIdPool = 0
                orderRequest.state = OrderRequestState.ACTIVE

                log.info("creating new orderRequest, orderRequest={}", v("orderRequest", orderRequest))
                this.orderRequestRepo.save(orderRequest)
                    .doOnSuccess {
                        log.info("new orderRequest created, orderRequest={}", v("orderRequest", orderRequest))
                    }
            }
    }

}
