package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.warehouse.SOURCE
import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.AvailabilityRepository
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.*


@Service
class ProductServiceAvailabilityDeleterImpl(
    private val clock: Clock,

    private val availabilityRepo: AvailabilityRepository,

    @Qualifier(KafkaConfig.TEMPLATE__AVAILABILITY)
    private val kafka: KafkaTemplate<UUID, AvailabilityProto.Availability>,

    private val finder: ProductServiceFinderImpl,
) {

    private val log = KotlinLogging.logger {}


    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
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
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))
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
            .completable()
            .join()
    }

}
