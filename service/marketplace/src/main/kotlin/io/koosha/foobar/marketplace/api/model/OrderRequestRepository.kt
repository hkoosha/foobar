package io.koosha.foobar.marketplace.api.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux


interface OrderRequestRepository : ReactiveCrudRepository<OrderRequestDO, String> {

    fun findAllByCustomerId(customerId: String): Flux<OrderRequestDO>

}
