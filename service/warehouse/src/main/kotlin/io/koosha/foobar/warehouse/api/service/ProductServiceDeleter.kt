package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.ProductRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Service
class ProductServiceDeleter(
    private val productRepo: ProductRepository,
    private val finder: ProductServiceFinder,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun delete(productId: UUID) {

        val maybeProduct: Optional<ProductDO> = this.finder.findById(productId)
        if (!maybeProduct.isPresent) {
            log.debug("not deleting product, entity does not exist, productId={}", productId)
            return
        }

        val product = maybeProduct.get()

        if (product.active!!) {
            log.debug("refused to delete product in current state, product={}", v("product", product))
            throw EntityInIllegalStateException(
                entityType = "product",
                entityId = productId,
                msg = "can not delete active product",
            )
        }

        log.info("deleting product, product={}", v("product", product))
        this.productRepo.delete(product)
    }

}
