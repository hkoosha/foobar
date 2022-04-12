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
        log.trace { "product not found, productId=$productId" }
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
            log.trace { "create product validation error: $errors" }
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

        log.info { "creating new product, product=$product" }
        this.productRepo.save(product)
        return product
    }


    private fun findAndApplyProductChanges(
        request: ProductUpdateRequest,
        product: ProductDO,
    ): Boolean {

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
            log.info { "updating product, productId=${product.productId} req=$request" }
        else
            log.trace { "nothing to update on product, productId=${product.productId}, req=$request" }

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
            log.trace { "update product validation error: $errors" }
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

        log.info { "updating product, productId=$productId request=$request" }
        entity.updated = this.clock.instant().atZone(ZoneOffset.UTC)
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
            log.debug { "refused to delete product in current state, product=$product" }
            throw EntityInIllegalStateException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                msg = "can not delete active product",
            )
        }

        log.info { "deleting product, product=$product" }
        this.productRepo.delete(product)
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequest,
    ): AvailabilityDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "add availability validation error: $errors" }
            throw EntityBadValueException(
                entityType = AvailabilityDO.ENTITY_TYPE,
                entityId = null,
                errors
            )
        }

        val product: ProductDO = this.findProductOrFail(productId)
        if (product.active != true) {
            log.debug { "refused to add availability in current state, product=$product, req=$request" }
            throw EntityInIllegalStateException(
                entityType = ProductDO.ENTITY_TYPE,
                entityId = productId,
                msg = "product is not active, can not add availability"
            )
        }

        log.trace { "fetching seller, sellerId=${request.sellerId}" }
        try {
            this.sellerClient.getSeller(request.sellerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug { "refused to add availability, seller not found, product=$product, req=$request" }
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

        val availability = AvailabilityDO()
        availability.availabilityPk.sellerId = request.sellerId
        availability.availabilityPk.product = product
        availability.unitsAvailable = request.unitsAvailable
        availability.frozenUnits = 0
        availability.pricePerUnit = request.pricePerUnit
        availability.created = this.clock.instant().atZone(ZoneOffset.UTC)
        availability.updated = availability.created

        if (availabilityRepo.findById(availability.availabilityPk).isPresent) {
            log.debug { "refused to add duplicated availability, product=$product, req=$request" }
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

        log.info {
            "adding product availability, productId=${product.productId} sellerId=${request.sellerId}, availability=$availability"
        }
        this.availabilityRepo.save(availability)

        log.trace {
            "sending new availability to kafka, " +
                    "productId=${product.productId} sellerId=${request.sellerId}, availability=$availability"
        }
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

        return availability
    }

    // TODO use error object, throw all errors at once.
    private fun findAndApplyAvailabilityChanges(
        request: AvailabilityUpdateRequest,
        product: ProductDO,
        availability: AvailabilityDO,
    ): Boolean {

        var anyChange = false

        if (request.unitsAvailable != null && request.unitsToFreeze != null) {
            log.debug {
                "update availability validation error: can not freeze and set availability unit at the same time," +
                        " product=$product, availability=$availability request=$request"
            }
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

        if (request.unitsAvailable != null) {

            if (request.unitsAvailable > availability.unitsAvailable!! && !product.active!!) {
                log.debug {
                    "update availability validation error: product is not active, can not increase availability," +
                            " product=$product, availability=$availability request=$request"
                }
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
                log.warn { "available units going under frozen units, product=$product availability=$availability request=$request" }

            availability.unitsAvailable = request.unitsAvailable
            anyChange = true
        }

        if (request.unitsToFreeze != null) {

            if (request.unitsToFreeze > availability.unitsAvailable!!) {
                log.debug {
                    "update availability validation error: not enough units to freeze," +
                            " product=$product, availability=$availability request=$request"
                }
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
            anyChange = true
        }

        if (request.pricePerUnit != null) {

            availability.pricePerUnit = request.pricePerUnit
            anyChange = true
        }

        return anyChange
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ): AvailabilityDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "update availability validation error: $errors" }
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

        val product = this.findProductOrFail(productId)
        val availability = this.availabilityRepo.findById(
            AvailabilityDO.AvailabilityPk(
                sellerId = sellerId,
                product = product,
            )
        ).orElseThrow {
            log.trace { "availability for update not found, productId=$productId, sellerId=$sellerId, request=$request" }
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
                ),
            )
        }

        val anyChange = this.findAndApplyAvailabilityChanges(
            request,
            product,
            availability,
        )
        if (!anyChange)
            return availability

        log.info { "updating product availability, productId=${product.productId} sellerId=$sellerId" }
        this.availabilityRepo.save(availability)

        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
            .setAction("update")
            .setSellerId(sellerId.toString())
            .setProductId(productId.toString())
            .setFrozenUnits(availability.frozenUnits!!)
            .setUnitsAvailable(availability.unitsAvailable!!)
            .setPricePerUnit(availability.pricePerUnit!!)
            .build()

        log.trace {
            "sending updated availability to kafka, " +
                    "productId=${product.productId} sellerId=$sellerId, availability=$availability"
        }
        this.kafka
            .sendDefault(send)
            .completable()
            .join()

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
            log.debug {
                "not deleting availability: entity does not exist, customerId=$productId, sellerId=$sellerId"
            }
            return
        }

        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
            .setAction("delete")
            .setSellerId(sellerId.toString())
            .setProductId(productId.toString())
            .build()

        log.info { "removing product availability, productId=${product.productId} sellerId=$sellerId" }
        this.availabilityRepo.delete(availability.get())

        log.trace {
            "sending removed availability to kafka, " +
                    "productId=${product.productId} sellerId=$sellerId, availability=$availability"
        }
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
