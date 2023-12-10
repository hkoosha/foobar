package io.koosha.foobar.marketplace.api.model.repo

import io.koosha.foobar.marketplace.api.model.entity.ProcessedOrderRequestSellerDO
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ProcessedOrderRequestSellerRepository : ReactiveCrudRepository<ProcessedOrderRequestSellerDO, String>
