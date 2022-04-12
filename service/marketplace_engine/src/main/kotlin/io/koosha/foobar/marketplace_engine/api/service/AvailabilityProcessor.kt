package io.koosha.foobar.marketplace_engine.api.service


import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace_engine.SOURCE
import io.koosha.foobar.marketplace_engine.api.model.AvailabilityDO
import io.koosha.foobar.marketplace_engine.api.model.AvailabilityRepository
import io.koosha.foobar.product.AvailabilityProto
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import javax.transaction.Transactional


@Component
class AvailabilityProcessor(
    private val availabilityRepo: AvailabilityRepository,
) {

    private val log = KotlinLogging.logger {}

    @KafkaListener(
        groupId = "${SOURCE}__availability",
        concurrency = "2",
        topics = [KafkaConfig.TOPIC__AVAILABILITY],
        containerFactory = KafkaConfig.LISTENER_CONTAINER_FACTORY__AVAILABILITY,
    )
    fun listenAvailability(
        @Payload payload: AvailabilityProto.Availability,
        ack: Acknowledgment,
    ) {

        this.storeAvailability(payload)
        ack.acknowledge()
    }

    @Transactional
    internal fun storeAvailability(payload: AvailabilityProto.Availability) {

        val availability = AvailabilityDO(
            availabilityPk = AvailabilityDO.Pk(
                payload.sellerId.toUUID(),
                payload.productId.toUUID(),
            ),
            version = null,
            created = null,
            updated = null,
            unitsAvailable = payload.unitsAvailable,
            frozenUnits = payload.frozenUnits,
            pricePerUnit = payload.pricePerUnit,
        )

        log.info { "adding availability: $availability" }
        this.availabilityRepo.save(availability)
    }

}
