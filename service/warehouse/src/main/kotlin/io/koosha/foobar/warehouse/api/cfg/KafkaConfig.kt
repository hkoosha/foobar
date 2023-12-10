package io.koosha.foobar.warehouse.api.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.product.AvailabilityProtoSerde
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.VoidSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.UUID

@Configuration
@EnableKafka
// @Profile(PROFILE__KAFKA)
class KafkaConfig {

    @Qualifier(KafkaDefinitions.PROD_FACTORY__AVAILABILITY)
    @Bean(KafkaDefinitions.PROD_FACTORY__AVAILABILITY)
    fun availabilityKafkaProducerFactory(
        prop: KafkaProperties,
    ): ProducerFactory<UUID, AvailabilityProto.Availability> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to VoidSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to AvailabilityProtoSerde.Ser::class.java,
            ),
        )

    @Qualifier(KafkaDefinitions.TEMPLATE__AVAILABILITY)
    @Bean(KafkaDefinitions.TEMPLATE__AVAILABILITY)
    fun availabilityKafkaTemplate(
        @Qualifier(KafkaDefinitions.PROD_FACTORY__AVAILABILITY)
        producerFactory: ProducerFactory<UUID, AvailabilityProto.Availability>,
    ): KafkaTemplate<UUID, AvailabilityProto.Availability> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaDefinitions.TOPIC__AVAILABILITY
        }

}
