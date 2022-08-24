package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.ZoneOffset
import java.util.*
import javax.validation.Validator


@Service
class ProductServiceCreatorImpl(
    private val clock: Clock,
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
            log.trace("create product validation error, errors={}", errors, kv("validationErrors", errors))
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
        product.created = this.clock.instant().atZone(ZoneOffset.UTC)
        product.updated = product.created

        log.info("creating new product, product={}", product, kv("product", product))
        this.productRepo.save(product)
        return product
    }

}
