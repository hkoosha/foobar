package io.koosha.foobar.marketplace.api.model.repo

import io.koosha.foobar.marketplace.api.model.entity.OrderRequestProcessQueueDO
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderRequestProcessQueueRepository : ReactiveCrudRepository<OrderRequestProcessQueueDO, String>
