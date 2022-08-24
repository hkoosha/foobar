package io.koosha.foobar.warehouse.api.service

import feign.FeignException
import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.warehouse.SOURCE
import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.AvailabilityRepository
import io.koosha.foobar.warehouse.api.model.ProductDO
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.*
import javax.validation.Validator


@Service
class ProductServiceAvailabilityAdderImpl(
    private val clock: Clock,
    private val validator: Validator,

    private val sellerClient: SellerApi,

    private val availabilityRepo: AvailabilityRepository,

    @Qualifier(KafkaConfig.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,

    private val finder: ProductServiceFinderImpl,
) {

    private val log = KotlinLogging.logger {}


    private fun addAvailabilityValidate(
        request: AvailabilityCreateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace("add availability validation error, errors={}", v("validationErrors", errors))
            throw EntityBadValueException(
                entityType = AvailabilityDO.ENTITY_TYPE,
                entityId = null,
                errors
            )
        }

    }

    private fun addAvailabilityFindProduct(
        productId: UUID,
        request: AvailabilityCreateRequest,
    ): ProductDO {

        val product: ProductDO = this.finder.findByIdOrFail(productId)

        if (product.active != true) {
            log.debug(
                "refused to add availability in current state, product={}, request={}",
                v("product", product),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                msg = "product is not active, can not add availability"
            )
        }

        return product
    }

    private fun addAvailabilityFetchAndEnsureSellerExists(
        product: ProductDO,
        request: AvailabilityCreateRequest,
    ) {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))

        try {
            this.sellerClient.getSeller(request.sellerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add availability, seller not found, product={}, request={}",
                v("product", product),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = SellerApi.ENTITY_TYPE,
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
        request: AvailabilityCreateRequest,
    ) {

        log.trace(
            "sending new availability to kafka, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )
        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
            .setAction("add")
            .setSellerId(availability.availabilityPk.sellerId.toString())
            .setProductId(product.productId.toString())
            .setUnitsAvailable(availability.unitsAvailable!!)
            .setFrozenUnits(availability.frozenUnits!!)
            .setPricePerUnit(availability.pricePerUnit!!)
        this.kafka
            .sendDefault(send.build())
            .completable()
            .join()
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequest,
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

        if (this.availabilityRepo.findById(availability.availabilityPk).isPresent) {
            log.debug(
                "refused to add duplicated availability, product={}, request={}",
                v("product", product),
                v("request", request),
            )
            throw EntityBadValueException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = productId,
                    ),
                    EntityInfo(
                        entityType = SellerApi.ENTITY_TYPE,
                        entityId = request.sellerId,
                    ),
                ),
                msg = "duplicate entry for availability"
            )
        }

        log.info(
            "adding product availability, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )
        this.availabilityRepo.save(availability)
        log.info(
            "product availability added, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", request.sellerId),
            v("availability", availability),
        )

        this.addAvailabilitySendKafka(product, availability, request)

        return availability
    }

}
