package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.AvailabilityRepository
import io.koosha.foobar.warehouse.api.model.ProductDO
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class ProductServiceAvailabilityFinderImpl(
    private val availabilityRepo: AvailabilityRepository,

    private val finder: ProductServiceFinderImpl,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(readOnly = true)
    fun getAvailabilitiesOf(productId: UUID): Iterable<AvailabilityDO> {

        val product = this.finder.findByIdOrFail(productId)
        return this.availabilityRepo.findAllByAvailabilityPk_Product(product)
    }

    @Transactional(readOnly = true)
    fun getAvailability(
        productId: UUID,
        sellerId: UUID,
    ): AvailabilityDO {

        val product = this.finder.findByIdOrFail(productId)

        return this.availabilityRepo.findByAvailabilityPk_ProductAndAvailabilityPk_SellerId(
            product,
            sellerId
        ).orElseThrow {

            this.log.trace(
                "availability not found, productId={} sellerId={}",
                v("productId", productId),
                v("sellerId", sellerId)
            )

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
