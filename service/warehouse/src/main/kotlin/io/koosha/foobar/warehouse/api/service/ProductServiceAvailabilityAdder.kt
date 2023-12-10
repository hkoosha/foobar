package io.koosha.foobar.warehouse.api.service

import feign.FeignException
import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.warehouse.api.connect.SellerApi
import io.koosha.foobar.warehouse.api.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.warehouse.api.model.dto.AvailabilityCreateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.AvailabilityRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.Clock
import java.util.UUID

@Service
class ProductServiceAvailabilityAdder(
    private val clock: Clock,
    private val validator: Validator,
    private val sellerClient: SellerApi,
    private val availabilityRepo: AvailabilityRepository,
    private val txTemplate: TransactionTemplate,
    private val finder: ProductServiceFinder,

    @Qualifier(KafkaDefinitions.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun addAvailabilityValidate(
        request: AvailabilityCreateRequestDto,
    ) {
        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "add availability validation error, request={}, errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "availability",
                entityId = null,
                errors
            )
        }
    }

    private fun addAvailabilityFindProduct(
        productId: UUID,
        request: AvailabilityCreateRequestDto,
    ): ProductDO {

        val product: ProductDO = this.finder.findByIdOrFail(productId)

        if (product.active != true) {
            log.debug(
                "refused to add availability in current state, product={}, request={}",
                v("product", product),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = "product",
                entityId = productId,
                msg = "product is not active, can not add availability"
            )
        }

        return product
    }

    private fun addAvailabilityFetchAndEnsureSellerExists(
        product: ProductDO,
        request: AvailabilityCreateRequestDto,
    ) {
        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))

        try {
            this.sellerClient.getSeller(request.sellerId!!)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add availability, seller not found, product={}, request={}",
                v("product", product),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = "seller",
                entityId = request.sellerId,
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching seller", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }
    }

    private fun addAvailabilitySendKafka(
        product: ProductDO,
        availability: AvailabilityDO,
        request: AvailabilityCreateRequestDto,
    ) {
        log.trace(
            "sending new availability to kafka, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )
        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create("warehouse", this.clock.millis()))
            .setAction("add")
            .setSellerId(availability.availabilityPk.sellerId.toString())
            .setProductId(product.productId.toString())
            .setUnitsAvailable(availability.unitsAvailable!!)
            .setFrozenUnits(availability.frozenUnits!!)
            .setPricePerUnit(availability.pricePerUnit!!)
        this.kafka
            .sendDefault(send.build())
            .join()
    }

    private fun saveAvailability(
        productId: UUID,
        request: AvailabilityCreateRequestDto,
        availability: AvailabilityDO,
    ) {
        try {
            this.txTemplate.execute {
                this.availabilityRepo.save(availability)
            }
        }
        catch (e: DataIntegrityViolationException) {
            throw if (this.isDuplicateEntry(e))
                EntityBadValueException(
                    context = setOf(
                        EntityInfo(
                            entityType = "product",
                            entityId = productId,
                        ),
                        EntityInfo(
                            entityType = "seller",
                            entityId = request.sellerId,
                        ),
                    ),
                    msg = "duplicate entry for availability"
                )
            else {
                e
            }
        }
    }

    fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequestDto,
    ): AvailabilityDO {

        this.addAvailabilityValidate(request)

        val product = this.addAvailabilityFindProduct(productId, request)
        this.addAvailabilityFetchAndEnsureSellerExists(product, request)

        val availability = AvailabilityDO()
        availability.availabilityPk.sellerId = request.sellerId
        availability.availabilityPk.product = product
        availability.unitsAvailable = request.unitsAvailable
        availability.frozenUnits = 0
        availability.pricePerUnit = request.pricePerUnit

        log.trace(
            "adding product availability, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )

        this.saveAvailability(productId, request, availability)

        log.info(
            "product availability added, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )

        this.addAvailabilitySendKafka(product, availability, request)

        return availability
    }

    private fun isDuplicateEntry(
        it: Throwable,
    ): Boolean {

        if (it !is DataIntegrityViolationException)
            return false

        return it.cause?.message?.contains("Duplicate entry") == true ||
                it.message?.contains("Duplicate entry") == true ||
                it.cause?.message?.contains("duplicate key value violates unique constraint") == true ||
                it.message?.contains("duplicate key value violates unique constraint") == true
    }

}
