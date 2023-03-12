package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import jakarta.validation.Validator


@Service
class ProductServiceUpdaterImpl(
    private val validator: Validator,

    private val productRepo: ProductRepository,

    private val finder: ProductServiceFinderImpl,
) {

    private val log = KotlinLogging.logger {}


    private fun findAndApplyProductChanges(
        request: ProductUpdateRequest,
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
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun update(
        productId: UUID,
        request: ProductUpdateRequest,
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
                entityType = ProductDO.ENTITY_TYPE,
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
