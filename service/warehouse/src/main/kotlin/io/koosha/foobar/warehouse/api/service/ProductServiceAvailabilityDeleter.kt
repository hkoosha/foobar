package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.repo.AvailabilityRepository
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
class ProductServiceAvailabilityDeleter(
    private val clock: Clock,
    private val availabilityRepo: AvailabilityRepository,
    private val finder: ProductServiceFinder,

    @Qualifier(KafkaDefinitions.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun deleteAvailability(
        productId: UUID,
        sellerId: UUID,
    ) {

        val product = this.finder.findByIdOrFail(productId)
        val availability = this.availabilityRepo.findById(
            AvailabilityDO.AvailabilityPk(
                sellerId = sellerId,
                product = product
            )
        )

        if (availability.isEmpty) {
            log.debug(
                "not deleting availability: entity does not exist, product={}, sellerId={}",
                v("product", product),
                v("sellerId", sellerId),
            )
            return
        }

        val send = AvailabilityProto.Availability
            .newBuilder()
            .setHeader(HeaderHelper.create("warehouse", this.clock.millis()))
            .setAction("delete")
            .setSellerId(sellerId.toString())
            .setProductId(productId.toString())
            .build()

        log.info(
            "removing product availability, product={} sellerId={}",
            v("product", product),
            v("sellerId", sellerId),
        )
        this.availabilityRepo.delete(availability.get())

        log.trace(
            "sending removed availability to kafka, product={} sellerId={}, availability={}",
            v("product", product),
            v("sellerId", sellerId),
            v("availability", availability),
        )
        this.kafka
            .sendDefault(send)
            .join()
    }

}
