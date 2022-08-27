package io.koosha.foobar.marketplace.api.model

import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface ProcessedOrderRequestSellerRepository : ReactiveCrudRepository<ProcessedOrderRequestSellerDO, String>
