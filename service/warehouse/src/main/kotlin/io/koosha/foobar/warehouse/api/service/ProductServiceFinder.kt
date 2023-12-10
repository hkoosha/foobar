package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.ProductRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Service
class ProductServiceFinder(
    private val productRepo: ProductRepository,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun findById(
        productId: UUID,
    ): Optional<ProductDO> =
        this.productRepo.findById(productId)

    @Transactional(readOnly = true)
    fun findByIdOrFail(
        productId: UUID,
    ): ProductDO =
        this.productRepo
            .findById(productId)
            .orElseThrow {
                this.log.trace("product not found, productId={}", v("productId", productId))
                EntityNotFoundException(
                    entityType = "product",
                    entityId = productId,
                )
            }

    @Transactional(readOnly = true)
    fun findAll(): Iterable<ProductDO> =
        this.productRepo.findAll()

}
