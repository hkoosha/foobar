package io.koosha.foobar.marketplace.api.service

import feign.FeignException
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.openapitools.client.model.Customer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator


@Service
class OrderRequestServiceCreationImpl(
    private val validator: Validator,

    private val customerClient: CustomerApi,

    private val orderRequestRepo: OrderRequestRepository,
) {

    private val log = KotlinLogging.logger {}

    private fun validate(
        request: OrderRequestCreateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("create orderRequest validation error, errors={}", errors, kv("validationErrors", errors))
            throw EntityBadValueException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }
    }

    private fun findCustomer(
        request: OrderRequestCreateRequest,
    ): Customer {

        log.trace("fetching customer, customerId={}", request.customerId, kv("customerId", request.customerId))

        val customer = try {
            this.customerClient.getCustomer(request.customerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug("refused to add orderRequest, customer not found, req={}", request, kv("request", request))
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
                customer,
                request,
                kv("customer", customer),
                kv("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = CustomerApi.ENTITY_TYPE,
                entityId = request.customerId,
                msg = "customer is not active",
            )
        }

        return customer
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun create(
        request: OrderRequestCreateRequest,
    ): OrderRequestDO {

        this.validate(request)

        // Make sure customer exists and is active.
        this.findCustomer(request)

        val orderRequest = OrderRequestDO()
        orderRequest.orderRequestId = UUID.randomUUID()
        orderRequest.customerId = request.customerId
        orderRequest.lineItemIdPool = 0
        orderRequest.state = OrderRequestState.ACTIVE

        log.info("creating new orderRequest, orderRequest={}", orderRequest, kv("orderRequest", orderRequest))
        this.orderRequestRepo.save(orderRequest)
        return orderRequest
    }

}
