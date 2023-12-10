package io.koosha.foobar.marketplace.api.model.repo

import io.koosha.foobar.marketplace.api.model.entity.OrderRequestLineItemDO
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderRequestLineItemRepository : ReactiveCrudRepository<OrderRequestLineItemDO, String> {

    fun deleteAllByOrderRequestId(
        orderRequestId: String
    ): Mono<Void?>

    fun findAllByOrderRequestId(
        orderRequestId: String
    ): Flux<OrderRequestLineItemDO>

    fun findByOrderRequestIdAndOrderRequestLineItemId(
        orderRequestId: String,
        orderRequestLineItemId: Long,
    ): Mono<OrderRequestLineItemDO>

}
