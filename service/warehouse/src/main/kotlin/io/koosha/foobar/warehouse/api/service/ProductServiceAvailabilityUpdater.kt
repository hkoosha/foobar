package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.warehouse.api.model.dto.AvailabilityUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.model.repo.AvailabilityRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
class ProductServiceAvailabilityUpdater(
    private val clock: Clock,
    private val validator: Validator,
    private val availabilityRepo: AvailabilityRepository,
    private val finder: ProductServiceFinder,

    @Qualifier(KafkaDefinitions.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun validate(
        request: AvailabilityUpdateRequestDto,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {
        if (request.unitsAvailable != null && request.unitsToFreeze != null) {
            log.debug(
                "update availability validation error: can not freeze and set availability unit at the same time," +
                        " product={}, availability={} request={}",
                v("product", product),
                v("availability", availability),
                v("request", request),
            )

            throw EntityBadValueException(
                context = setOf(
                    EntityInfo(
                        entityType = "product",
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = availability.availabilityPk.sellerId,
                    ),
                ),
                msg = "can not freeze and set available units at the same time"
            )
        }
    }

    private fun unitsAvailable(
        request: AvailabilityUpdateRequestDto,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {
        if (request.unitsAvailable!! > availability.unitsAvailable!! && !product.active!!) {
            log.debug(
                "update availability validation error: product is not active, can not increase availability," +
                        " product={}, availability={} request={}",
                v("product", product),
                v("availability", availability),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                context = setOf(
                    EntityInfo(
                        entityType = "product",
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = availability.availabilityPk.sellerId,
                    ),
                ),
                msg = "product is not active, can not increase availability",
            )
        }

        if (request.unitsAvailable < availability.frozenUnits!!)
            log.warn(
                "available units going under frozen units, product={} availability={} request={}",
                v("product", product),
                v("availability", availability),
                v("request", request),
            )

        availability.unitsAvailable = request.unitsAvailable
    }

    private fun unitsToFreeze(
        request: AvailabilityUpdateRequestDto,
        product: ProductDO,
        availability: AvailabilityDO,
    ) {
        if (request.unitsToFreeze!! > availability.unitsAvailable!!) {
            log.debug(
                "update availability validation error: not enough units to freeze, " +
                        "product={} availability={} request={}",
                v("product", product),
                v("availability", availability),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                context = setOf(
                    EntityInfo(
                        entityType = "product",
                        entityId = product.productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = availability.availabilityPk.sellerId,
                    ),
                    EntityInfo(
                        entityType = "availability",
                        entityId = null,
                    ),
                ),
                msg = "not enough units to freeze",
            )
        }

        availability.frozenUnits = request.unitsToFreeze
    }

    private fun findAndApplyAvailabilityChanges(
        request: AvailabilityUpdateRequestDto,
        product: ProductDO,
        availability: AvailabilityDO,
    ): Boolean {

        this.validate(request, product, availability)

        var anyChange = false

        if (request.unitsAvailable != null) {
            this.unitsAvailable(request, product, availability)
            anyChange = true
        }
        if (request.unitsToFreeze != null) {
            this.unitsToFreeze(request, product, availability)
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
        request: AvailabilityUpdateRequestDto,
    ) {
        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update availability validation error, productId={} sellerId={} errors={}",
                v("productId", productId),
                v("sellerId", sellerId),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                context = setOf(
                    EntityInfo(
                        entityType = "product",
                        entityId = productId,
                    ),
                    EntityInfo(
                        entityType = "seller",
                        entityId = sellerId,
                    ),
                ),
                errors
            )
        }
    }

    private fun findAvailability(
        product: ProductDO,
        sellerId: UUID,
        request: AvailabilityUpdateRequestDto,
    ): AvailabilityDO =
        this.availabilityRepo
            .findById(
                AvailabilityDO.AvailabilityPk(
                    sellerId = sellerId,
                    product = product,
                )
            )
            .orElseThrow {
                log.trace(
                    "availability for update not found, product={}, sellerId={}, request={}",
                    v("product", product),
                    v("sellerId", sellerId),
                    v("request", request),
                )
                EntityNotFoundException(
                    context = setOf(
                        EntityInfo(
                            entityType = "product",
                            entityId = product.productId,
                        ),
                        EntityInfo(
                            entityType = "seller",
                            entityId = sellerId,
                        ),
                    ),
                )
            }

    private fun sendKafka(
        product: ProductDO,
        availability: AvailabilityDO,
        sellerId: UUID,
    ) {
        val send: AvailabilityProto.Availability = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create("warehouse", this.clock.millis()))
            .setAction("update")
            .setSellerId(sellerId.toString())
            .setProductId(product.productId.toString())
            .setFrozenUnits(availability.frozenUnits!!)
            .setUnitsAvailable(availability.unitsAvailable!!)
            .setPricePerUnit(availability.pricePerUnit!!)
            .build()

        log.trace(
            "sending updated availability to kafka, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", sellerId),
            v("availability", availability),
        )

        this.kafka
            .sendDefault(send)
            .join()
    }

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequestDto,
    ): AvailabilityDO {

        this.updateAvailabilityValidate(productId, sellerId, request)

        val product: ProductDO = this.finder.findByIdOrFail(productId)
        val availability: AvailabilityDO = this.findAvailability(product, sellerId, request)

        val anyChange: Boolean = this.findAndApplyAvailabilityChanges(
            request,
            product,
            availability,
        )

        if (!anyChange)
            return availability

        log.info(
            "updating product availability, product={} sellerId={}",
            v("product", product),
            v("sellerId", sellerId),
        )

        this.availabilityRepo.save(availability)
        this.sendKafka(product, availability, sellerId)

        return availability
    }

}
