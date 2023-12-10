package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.warehouse.api.model.dto.ProductCreateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.ProductRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProductServiceCreator(
    private val validator: Validator,
    private val productRepo: ProductRepository,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun create(request: ProductCreateRequestDto): ProductDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create product validation error, request={} errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "product",
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

        log.trace("creating new product, product={}", v("product", product))
        this.productRepo.save(product)
        log.info("new product created, product={}", v("product", product))
        return product
    }

}
