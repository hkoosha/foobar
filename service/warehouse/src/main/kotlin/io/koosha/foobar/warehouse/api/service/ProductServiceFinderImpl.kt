package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductServiceFinderImpl(
    private val productRepo: ProductRepository,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(readOnly = true)
    fun findById(productId: UUID): Optional<ProductDO> = this.productRepo.findById(productId)

    @Transactional(readOnly = true)
    fun findByIdOrFail(productId: UUID): ProductDO = this.productRepo
        .findById(productId)
        .orElseThrow {
            this.log.trace("product not found, productId={}", v("productId", productId))
            EntityNotFoundException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
            )
        }

    @Transactional(readOnly = true)
    fun findAll(): Iterable<ProductDO> = this.productRepo.findAll()

}
