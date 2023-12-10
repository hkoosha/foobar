package io.koosha.foobar.marketplaceengine.api.service

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplaceengine.api.model.entity.AvailabilityDO
import io.koosha.foobar.marketplaceengine.api.model.repo.AvailabilityRepository
import io.koosha.foobar.product.AvailabilityProto
import io.micrometer.core.annotation.Timed
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
class AvailabilityProcessor(
    private val availabilityRepo: AvailabilityRepository,
) {

    companion object {
        private const val KAFKA_TIMEOUT_MILLIS = 3000L
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        log.info("listening to: {}", KafkaDefinitions.TOPIC__AVAILABILITY)
    }

    @Timed(extraTags = [TAG, TAG_VALUE])
    @KafkaListener(
        groupId = "marketplace_engine__availability",
        concurrency = "2",
        topics = [KafkaDefinitions.TOPIC__AVAILABILITY],
        containerFactory = KafkaDefinitions.LISTENER_CONTAINER_FACTORY__AVAILABILITY,
    )
    fun listenAvailability(
        @Payload payload: AvailabilityProto.Availability,
        ack: Acknowledgment,
    ) {

        try {
            this.storeAvailability(payload)
            ack.acknowledge()
        }
        catch (e: Exception) {
            log.error("error adding availability: {} -> {}", e::class.java, e.message)
            ack.nack(Duration.ofMillis(KAFKA_TIMEOUT_MILLIS))
        }
    }

    @Transactional
    internal fun storeAvailability(payload: AvailabilityProto.Availability) {

        val id = AvailabilityDO.AvailabilityPk(
            payload.sellerId.toUUID(),
            payload.productId.toUUID(),
        )

        val existing = this.availabilityRepo.findById(id)

        val toSave = if (existing.isPresent) {
            val availability = existing.get()
            availability.unitsAvailable = payload.unitsAvailable
            availability.frozenUnits = payload.frozenUnits
            availability.pricePerUnit = payload.pricePerUnit
            availability
        }
        else {
            AvailabilityDO(
                availabilityPk = id,
                version = null,
                created = null,
                updated = null,
                unitsAvailable = payload.unitsAvailable,
                frozenUnits = payload.frozenUnits,
                pricePerUnit = payload.pricePerUnit,
            )
        }

        log.info("adding availability={}", v("availability", toSave))
        this.availabilityRepo.save(toSave)
    }

}
