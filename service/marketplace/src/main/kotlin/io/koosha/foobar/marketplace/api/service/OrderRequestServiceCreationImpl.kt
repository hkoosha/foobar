package io.koosha.foobar.marketplace.api.service

import feign.FeignException
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.connect.customer.rx.generated.api.Customer
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi
import io.koosha.foobar.marketplace.api.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Validator


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

        val customer = try {
            this.customerClient.getCustomer(request.customerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug("refused to add orderRequest, customer not found, request={}", v("request", request))
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
            log.debug(
                "refused to create order request in current state of customer, customer={}, request={}",
                v("customer", customer),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = CustomerApi.ENTITY_TYPE,
                entityId = request.customerId,
                msg = "customer is not active",
            )
        }

        // FIXME blocking call
        return Mono.just(customer)
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
