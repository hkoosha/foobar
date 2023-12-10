package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.repo.AvailabilityRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProductServiceAvailabilityFinder(
    private val availabilityRepo: AvailabilityRepository,
    private val finder: ProductServiceFinder,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    fun getAvailabilitiesOf(
        productId: UUID,
    ): Iterable<AvailabilityDO> {

        val product = this.finder.findByIdOrFail(productId)
        val availability = this.availabilityRepo.findAllByAvailabilityPk_Product(product)

        return availability
    }

    @Transactional(readOnly = true)
    fun getAvailability(
        productId: UUID,
        sellerId: UUID,
    ): AvailabilityDO {

        val product = this.finder.findByIdOrFail(productId)

        return this
            .availabilityRepo.findByAvailabilityPk_ProductAndAvailabilityPk_SellerId(
                product,
                sellerId,
            )
            .orElseThrow {
                this.log.trace(
                    "availability not found, productId={} sellerId={}",
                    v("productId", productId),
                    v("sellerId", sellerId)
                )
                EntityNotFoundException(
                    context = setOf(
                        EntityInfo(
                            entityType = "product",
                            entityId = productId,
                        ),
                        EntityInfo(
                            entityType = "seller",
                            entityId = sellerId,
                        ),
                        EntityInfo(
                            entityType = "availability",
                            entityId = null,
                        ),
                    )
                )
            }
    }

}
