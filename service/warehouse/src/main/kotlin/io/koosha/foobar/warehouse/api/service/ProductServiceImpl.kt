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
import io.koosha.foobar.warehouse.api.model.ProductRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.ZoneOffset
import java.util.*
import javax.validation.Validator


@Service
class ProductServiceImpl(
    private val clock: Clock,
    private val validator: Validator,
    private val sellerClient: SellerApi,
    private val productRepo: ProductRepository,
    private val availabilityRepo: AvailabilityRepository,
    @Qualifier(KafkaConfig.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,
) : ProductService {

    private val log = KotlinLogging.logger {}

    private fun findProductOrFail(productId: UUID): ProductDO = this.productRepo.findById(productId).orElseThrow {
        log.trace("product not found, productId={}", productId, kv("productId", productId))
        EntityNotFoundException(
            entityType = ProductDO.ENTITY_TYPE,
            entityId = productId,
        )
    }


    @Transactional(readOnly = true)
    override fun findById(productId: UUID): Optional<ProductDO> = this.productRepo.findById(productId)

    @Transactional(readOnly = true)
    override fun findByIdOrFail(productId: UUID): ProductDO = this.findProductOrFail(productId)

    @Transactional(readOnly = true)
    override fun findAll(): Iterable<ProductDO> = this.productRepo.findAll()

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun create(request: ProductCreateRequest): ProductDO {

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
                originalProduct,
                request,
                kv("product", originalProduct),
                kv("request", request),
            )
        else
            log.trace(
                "nothing to update on product, product={}, request={}",
                product,
                request,
                kv("product", product),
                kv("request", request),
            )

        return anyChange
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun update(
        productId: UUID,
        request: ProductUpdateRequest,
    ): ProductDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update product validation error,  productId={}, errors={}",
                productId,
                errors,
                kv("productId", productId),
                kv("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                errors
            )
        }

        val entity: ProductDO = this.findProductOrFail(productId)
        val anyChange = this.findAndApplyProductChanges(request, entity)
        if (!anyChange)
            return entity

        this.productRepo.save(entity)
        return entity
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun delete(productId: UUID) {

        val maybeProduct: Optional<ProductDO> = this.findById(productId)
        if (!maybeProduct.isPresent) {
            log.debug { "not deleting product, entity does not exist, productId=$productId" }
            return
        }

        val product = maybeProduct.get()

        if (product.active!!) {
            log.debug(
                "refused to delete product in current state, product={}",
                product,
                kv("product", product),
            )
            throw EntityInIllegalStateException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                msg = "can not delete active product",
            )
        }

        log.info(
            "deleting product, product={}",
            product,
            kv("product", product),
        )
        this.productRepo.delete(product)
    }


    private fun addAvailabilityValidate(
        request: AvailabilityCreateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "add availability validation error:, errors={}",
                errors,
                kv("validationErrors", errors)
            )
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

        val product: ProductDO = this.findProductOrFail(productId)

        if (product.active != true) {
            log.debug(
                "refused to add availability in current state, product={}, request={}",
                product,
                request,
                kv("product", product),
                kv("request", request),
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

        log.trace("fetching seller, sellerId={}", request.sellerId, kv("sellerId", request.sellerId))

        try {
            this.sellerClient.getSeller(request.sellerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add availability, seller not found, product={}, request={}",
                product,
                request,
                kv("product", product),
                kv("request", request),
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
            "sending new availability to kafka, " +
                    "product={} sellerId={}, availability={}",
            product,
            request.sellerId,
            availability,
            kv("product", product),
            kv("sellerId", request.sellerId),
            kv("availability", availability),
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
    override fun addAvailability(
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
        availability.created = this.clock.instant().atZone(ZoneOffset.UTC)
        availability.updated = availability.created

        if (availabilityRepo.findById(availability.availabilityPk).isPresent) {
            log.debug(
                "refused to add duplicated availability, product={}, request={}",
                product,
                request,
                kv("product", product),
                kv("request", request),
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
            product,
            request.sellerId,
            availability,
            kv("product", product),
            kv("sellerId", request.sellerId),
            kv("availability", availability),
        )
        this.availabilityRepo.save(availability)

        this.addAvailabilitySendKafka(product, availability, request)

        return availability
    }


    private fun findAndApplyAvailabilityChangesValidate(
        request: AvailabilityUpdateRequest,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {

        if (request.unitsAvailable != null && request.unitsToFreeze != null) {
            log.debug(
                "update availability validation error: can not freeze and set availability unit at the same time," +
                        " product={}, availability={} request={}",
                product,
                availability,
                request,
                kv("product", product),
                kv("availability", availability),
                kv("request", request),
            )
            throw EntityBadValueException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = SellerApi.ENTITY_TYPE,
                        entityId = availability.availabilityPk.sellerId,
                    ),
                ),
                msg = "can not freeze and set available units at the same time"
            )
        }

    }

    private fun findAndApplyAvailabilityUnitsAvailable(
        request: AvailabilityUpdateRequest,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {

        if (request.unitsAvailable!! > availability.unitsAvailable!! && !product.active!!) {
            log.debug(
                "update availability validation error: product is not active, can not increase availability," +
                        " product={}, availability={} request={}",
                product,
                availability,
                request,
                kv("product", product),
                kv("availability", availability),
                kv("request", request),
            )
            throw EntityInIllegalStateException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = SellerApi.ENTITY_TYPE,
                        entityId = availability.availabilityPk.sellerId,
                    ),
                ),
                msg = "product is not active, can not increase availability",
            )
        }

        if (request.unitsAvailable < availability.frozenUnits!!)
            log.warn(
                "available units going under frozen units, " +
                        "product={} availability={} request={}",
                product,
                availability,
                request,
                kv("product", product),
                kv("availability", availability),
                kv("request", request),
            )

        availability.unitsAvailable = request.unitsAvailable
    }

    private fun findAndApplyAvailabilityUnitsToFreeze(
        request: AvailabilityUpdateRequest,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {

        if (request.unitsToFreeze!! > availability.unitsAvailable!!) {
            log.debug(
                "update availability validation error: not enough units to freeze," +
                        "product={} availability={} request={}",
                product,
                availability,
                request,
                kv("product", product),
                kv("availability", availability),
                kv("request", request),
            )
            throw EntityInIllegalStateException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = SellerApi.ENTITY_TYPE,
                        entityId = availability.availabilityPk.sellerId,
                    ),
                    EntityInfo(
                        entityType = AvailabilityDO.ENTITY_TYPE,
                        entityId = null,
                    ),
                ),
                msg = "not enough units to freeze",
            )
        }

        availability.frozenUnits = request.unitsToFreeze
    }

    private fun findAndApplyAvailabilityChanges(
        request: AvailabilityUpdateRequest,
        product: ProductDO,
        availability: AvailabilityDO,
    ): Boolean {

        this.findAndApplyAvailabilityChangesValidate(request, product, availability)

        var anyChange = false

        if (request.unitsAvailable != null) {
            this.findAndApplyAvailabilityUnitsAvailable(request, product, availability)
            anyChange = true
        }
        if (request.unitsToFreeze != null) {
            this.findAndApplyAvailabilityUnitsToFreeze(request, product, availability)
            anyChange = true
        }

        if (request.pricePerUnit != null) {

            availability.pricePerUnit = request.pricePerUnit
            anyChange = true
        }

        return anyChange
    }

    private fun updateAvailabilityValidate(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update availability validation error, errors={}", errors,
                kv("validationErrors", errors),
                kv("productId", productId),
                kv("sellerId", sellerId),
                kv("request", request),
            )
            throw EntityBadValueException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = productId,
                    ),
                    EntityInfo(
                        entityType = SellerApi.ENTITY_TYPE,
                        entityId = sellerId,
                    ),
                ),
                errors
            )
        }
    }

    private fun updateAvailabilityFindAvailability(
        product: ProductDO,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ): AvailabilityDO = this
        .availabilityRepo
        .findById(
            AvailabilityDO.AvailabilityPk(
                sellerId = sellerId,
                product = product,
            )
        )
        .orElseThrow {
            log.trace(
                "availability for update not found, product={}, sellerId={}, request={}",
                product,
                sellerId,
                request,
                kv("product", product),
                kv("sellerId", sellerId),
                kv("request", request),
            )
            EntityNotFoundException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = sellerId,
                    ),
                ),
            )
        }

    private fun updateAvailabilitySendKafka(
        product: ProductDO,
        availability: AvailabilityDO,
        sellerId: UUID,
    ) {

        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
            .setAction("update")
            .setSellerId(sellerId.toString())
            .setProductId(product.productId.toString())
            .setFrozenUnits(availability.frozenUnits!!)
            .setUnitsAvailable(availability.unitsAvailable!!)
            .setPricePerUnit(availability.pricePerUnit!!)
            .build()

        log.trace(
            "sending updated availability to kafka, product={} sellerId={}, availability={}",
            product,
            sellerId,
            availability,
            kv("product", product),
            kv("sellerId", sellerId),
            kv("availability", availability),
        )
        this.kafka
            .sendDefault(send)
            .completable()
            .join()

    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ): AvailabilityDO {

        this.updateAvailabilityValidate(productId, sellerId, request)

        val product = this.findProductOrFail(productId)
        val availability = this.updateAvailabilityFindAvailability(product, sellerId, request)

        val anyChange = this.findAndApplyAvailabilityChanges(
            request,
            product,
            availability,
        )
        if (!anyChange)
            return availability

        log.info(
            "updating product availability, product={} sellerId={}",
            product,
            sellerId,
            kv("product", product),
            kv("sellerId", sellerId),
        )
        this.availabilityRepo.save(availability)
        this.updateAvailabilitySendKafka(product, availability, sellerId)

        return availability
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun deleteAvailability(
        productId: UUID,
        sellerId: UUID,
    ) {

        val product = this.findProductOrFail(productId)
        val availability = this.availabilityRepo.findById(
            AvailabilityDO.AvailabilityPk(
                sellerId = sellerId,
                product = product
            )
        )

        if (availability.isEmpty) {
            log.debug(
                "not deleting availability: entity does not exist, product={}, sellerId={}",
                product,
                sellerId,
                kv("product", product),
                kv("sellerId", sellerId),
            )
            return
        }

        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
            .setAction("delete")
            .setSellerId(sellerId.toString())
            .setProductId(productId.toString())
            .build()

        log.info(
            "removing product availability, product={} sellerId={}",
            product,
            sellerId,
            kv("product", product),
            kv("sellerId", sellerId),
        )
        this.availabilityRepo.delete(availability.get())

        log.trace(
            "sending removed availability to kafka, product={} sellerId={}, availability={}",
            product,
            sellerId,
            availability,
            kv("product", product),
            kv("sellerId", sellerId),
            kv("availability", availability),
        )
        this.kafka
            .sendDefault(send)
            .completable()
            .join()
    }

    @Transactional(readOnly = true)
    override fun getAvailabilitiesOf(productId: UUID): Iterable<AvailabilityDO> {

        val product = this.findProductOrFail(productId)
        return this.availabilityRepo.findAllByAvailabilityPk_Product(product)
    }

    @Transactional(readOnly = true)
    override fun getAvailability(
        productId: UUID,
        sellerId: UUID,
    ): AvailabilityDO {

        val product = this.findProductOrFail(productId)
        return this.availabilityRepo.findByAvailabilityPk_ProductAndAvailabilityPk_SellerId(
            product,
            sellerId
        ).orElseThrow {
            EntityNotFoundException(
                context = setOf(
                    EntityInfo(
                        entityType = ProductDO.ENTITY_TYPE,
                        entityId = productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = sellerId,
                    ),
                    EntityInfo(
                        entityType = AvailabilityDO.ENTITY_TYPE,
                        entityId = null,
                    ),
                )
            )
        }
    }

}
