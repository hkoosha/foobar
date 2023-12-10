package io.koosha.foobar.marketplace.api.model.repo

import io.koosha.foobar.marketplace.api.model.entity.OrderRequestDO
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface OrderRequestRepository : ReactiveCrudRepository<OrderRequestDO, String> {

    fun findAllByCustomerId(
        customerId: String
    ): Flux<OrderRequestDO>

}
