package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.stereotype.Service
import java.util.*


@Service
final class OrderRequestServiceFindingImpl(
    private val orderRequestRepo: OrderRequestRepository,
) {

    private val log = KotlinLogging.logger {}


    fun findById(orderRequestId: UUID): Optional<OrderRequestDO> =
        this.orderRequestRepo.findById(orderRequestId)

    fun findByIdOrFail(orderRequestId: UUID): OrderRequestDO = this.orderRequestRepo
        .findById(orderRequestId)
        .orElseThrow {
            this.log.trace(
                "orderRequest not found, orderRequestId={}",
                orderRequestId,
                kv("orderRequestId", orderRequestId)
            )
            EntityNotFoundException(
                entityType = OrderRequestDO.ENTITY_TYPE,
                entityId = orderRequestId,
            )
        }

    fun findAll(): Iterable<OrderRequestDO> = this.orderRequestRepo.findAll()

    fun findAllOrderRequestsOfCustomer(customerId: UUID): Iterable<OrderRequestDO> =
        this.orderRequestRepo.findAllByCustomerId(customerId)

}
