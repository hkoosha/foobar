package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator


@Service
class ProductServiceCreatorImpl(
    private val validator: Validator,

    private val productRepo: ProductRepository,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun create(request: ProductCreateRequest): ProductDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("create product validation error, errors={}", v("validationErrors", errors))
            throw EntityBadValueException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = null,
                errors
            )
        }

        val product = ProductDO()
        product.productId = UUID.randomUUID()
        product.active = request.active
        product.name = request.name
        product.unitSingle = request.unitSingle
        product.unitMultiple = request.unitMultiple

        log.info("creating new product, product={}", v("product", product))
        this.productRepo.save(product)
        log.info("new product created, product={}", v("product", product))
        return product
    }

}
