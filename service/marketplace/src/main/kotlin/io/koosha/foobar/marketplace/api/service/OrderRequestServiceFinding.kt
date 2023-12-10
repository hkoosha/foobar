package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.repo.OrderRequestRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
final class OrderRequestServiceFinding(
    private val orderRequestRepo: OrderRequestRepository,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun findById(orderRequestId: UUID): Mono<OrderRequestDO> =
        this.orderRequestRepo.findById(orderRequestId.toString())

    fun findByIdOrFail(orderRequestId: UUID): Mono<OrderRequestDO> =
        this.orderRequestRepo
            .findById(orderRequestId.toString())
            .switchIfEmpty(Mono.defer {
                this.log.trace("orderRequest not found, orderRequestId={}", v("orderRequestId", orderRequestId))
                Mono.error(
                    EntityNotFoundException(
                        entityType = "order_request",
                        entityId = orderRequestId,
                    )
                )
            })

    fun findAll(): Flux<OrderRequestDO> = this.orderRequestRepo.findAll()

    fun findAllOrderRequestsOfCustomer(customerId: UUID): Flux<OrderRequestDO> =
        this.orderRequestRepo.findAllByCustomerId(customerId.toString())

}
