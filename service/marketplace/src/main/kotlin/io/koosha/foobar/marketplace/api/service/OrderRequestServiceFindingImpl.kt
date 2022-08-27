package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@Service
final class OrderRequestServiceFindingImpl(
    private val orderRequestRepo: OrderRequestRepository,
) {

    private val log = KotlinLogging.logger {}


    fun findById(orderRequestId: UUID): Mono<OrderRequestDO> =
        this.orderRequestRepo.findById(orderRequestId.toString())

    fun findByIdOrFail(orderRequestId: UUID): Mono<OrderRequestDO> =
        this.orderRequestRepo
            .findById(orderRequestId.toString())
            .switchIfEmpty(Mono.defer {
                this.log.trace("orderRequest not found, orderRequestId={}", v("orderRequestId", orderRequestId))
                Mono.error(
                    EntityNotFoundException(
                        entityType = OrderRequestDO.ENTITY_TYPE,
                        entityId = orderRequestId,
                    )
                )
            })

    fun findAll(): Flux<OrderRequestDO> = this.orderRequestRepo.findAll()

    fun findAllOrderRequestsOfCustomer(customerId: UUID): Flux<OrderRequestDO> =
        this.orderRequestRepo.findAllByCustomerId(customerId.toString())

}
