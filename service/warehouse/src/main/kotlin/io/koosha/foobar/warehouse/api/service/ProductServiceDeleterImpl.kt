package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class ProductServiceDeleterImpl(
    private val productRepo: ProductRepository,

    private val finder: ProductServiceFinderImpl,
) {

    private val log = KotlinLogging.logger {}

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun delete(productId: UUID) {

        val maybeProduct: Optional<ProductDO> = this.finder.findById(productId)
        if (!maybeProduct.isPresent) {
            log.debug { "not deleting product, entity does not exist, productId=$productId" }
            return
        }

        val product = maybeProduct.get()

        if (product.active!!) {
            log.debug("refused to delete product in current state, product={}", v("product", product))
            throw EntityInIllegalStateException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                msg = "can not delete active product",
            )
        }

        log.info("deleting product, product={}", v("product", product))
        this.productRepo.delete(product)
    }

}
