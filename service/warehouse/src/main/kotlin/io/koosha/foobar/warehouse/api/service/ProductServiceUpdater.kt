package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.warehouse.api.model.dto.ProductUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.ProductRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProductServiceUpdater(
    private val validator: Validator,
    private val productRepo: ProductRepository,
    private val finder: ProductServiceFinder,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun findAndApplyProductChanges(
        request: ProductUpdateRequestDto,
        product: ProductDO,
    ): Boolean {

        val originalProduct = product.detachedCopy()

        var anyChange = false

        if (request.name != null && request.name != product.name) {
            product.name = request.name
            anyChange = true
        }
        if (request.active != null && request.active != product.active) {
            product.active = request.active
            anyChange = true
        }
        if (request.unitSingle != null && request.unitSingle != product.unitSingle) {
            product.unitSingle = request.unitSingle
            anyChange = true
        }
        if (request.unitMultiple != null && request.unitMultiple != product.unitMultiple) {
            product.unitMultiple = request.unitMultiple
            anyChange = true
        }

        if (anyChange)
            log.info(
                "updating product, product={} request={}",
                v("product", originalProduct),
                v("request", request),
            )
        else
            log.trace(
                "nothing to update on product, product={}, request={}",
                v("product", product),
                v("request", request),
            )

        return anyChange
    }

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun update(
        productId: UUID,
        request: ProductUpdateRequestDto,
    ): ProductDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update product validation error, productId={} request={} errors={}",
                v("productId", productId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "product",
                entityId = productId,
                errors
            )
        }

        val entity: ProductDO = this.finder.findByIdOrFail(productId)
        val anyChange = this.findAndApplyProductChanges(request, entity)
        if (!anyChange)
            return entity

        this.productRepo.save(entity)
        return entity
    }

}
