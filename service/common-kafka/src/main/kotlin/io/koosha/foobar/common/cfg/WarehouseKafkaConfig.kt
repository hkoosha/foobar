package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.product.AvailabilityProtoSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.VoidSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import java.util.*


@Profile(PROFILE__KAFKA)
@Configuration
class WarehouseKafkaConfig {

    @Qualifier(KafkaConfig.PROD_FACTORY__AVAILABILITY)
    @Lazy
    @Bean(KafkaConfig.PROD_FACTORY__AVAILABILITY)
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

    @Qualifier(KafkaConfig.TEMPLATE__AVAILABILITY)
    @Lazy
    @Bean(KafkaConfig.TEMPLATE__AVAILABILITY)
    fun availabilityKafkaTemplate(
        @Qualifier(KafkaConfig.PROD_FACTORY__AVAILABILITY)
        producerFactory: ProducerFactory<UUID, AvailabilityProto.Availability>,
    ): KafkaTemplate<UUID, AvailabilityProto.Availability> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaConfig.TOPIC__AVAILABILITY
        }

    @Qualifier(KafkaConfig.LISTENER_CONTAINER_FACTORY__AVAILABILITY)
    @Bean(KafkaConfig.LISTENER_CONTAINER_FACTORY__AVAILABILITY)
    fun availabilityKafkaListenerContainerFactory(
        @Qualifier(KafkaConfig.CONSUMER_FACTORY__AVAILABILITY)
        consumerFactory: ConsumerFactory<UUID, AvailabilityProto.Availability>,
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<UUID, AvailabilityProto.Availability>> {
        return ConcurrentKafkaListenerContainerFactory<UUID, AvailabilityProto.Availability>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(4)
            this.containerProperties.pollTimeout = 3000
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }

    @Qualifier(KafkaConfig.CONSUMER_FACTORY__AVAILABILITY)
    @Lazy
    @Bean(KafkaConfig.CONSUMER_FACTORY__AVAILABILITY)
    fun availabilityKafkaListenerConsumerFactory(
        prop: KafkaProperties,
    ): ConsumerFactory<UUID, AvailabilityProto.Availability> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to AvailabilityProtoSerde.Deser::class.java,
            ),
        )

}
