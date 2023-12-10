package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.marketplace.api.cfg.prop.ServicesProperties
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.dto.CustomerDto
import io.koosha.foobar.marketplace.api.model.dto.OrderRequestCreateRequestDto
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Clock
import java.util.UUID

@Service
class OrderRequestServiceCreation(
    private val validator: Validator,
    private val services: ServicesProperties,
    private val webClient: WebClient,
    private val orderRequestRepo: OrderRequestRepository,
    private val clock: Clock,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun customerGetApiAddr(
        customerId: UUID,
    ): URI =
        URI.create(this.services.customer().address() + "/foobar/customer/v1/customer/$customerId")

    private fun findCustomer(
        request: OrderRequestCreateRequestDto,
    ): Mono<CustomerDto> {

        log.trace("fetching customer, customerId={}", v("customerId", request.customerId))

        return this
            .webClient
            .get()
            .uri(this.customerGetApiAddr(request.customerId!!))
            .retrieve()
            .bodyToMono(CustomerDto::class.java)
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
                                    entityType = "customer",
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
                        entityType = "customer",
                        entityId = request.customerId,
                    )
                else
                    it
            }
    }

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun create(
        request: OrderRequestCreateRequestDto,
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
                    entityType = "order_request",
                    entityId = null,
                    errors,
                )
            )
        }

        return this
            .findCustomer(request)
            .flatMap {
                val orderRequest = OrderRequestDO()
                orderRequest.created = this.clock.instant()
                orderRequest.updated = orderRequest.created
                orderRequest.orderRequestId = UUID.randomUUID().toString()
                orderRequest.customerId = request.customerId.toString()
                orderRequest.lineItemIdPool = 0
                orderRequest.state = OrderRequestState.ACTIVE

                log.info("creating new orderRequest, orderRequest={}", v("orderRequest", orderRequest))

                this.orderRequestRepo
                    .save(orderRequest)
                    .doOnSuccess {
                        log.info("new orderRequest created, orderRequest={}", v("orderRequest", orderRequest))
                    }
            }
    }

}
