package io.koosha.foobar.marketplaceengine.api.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.entity.DeadLetterErrorProto
import io.koosha.foobar.entity.DeadLetterErrorProtoSerde
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.order_request.OrderRequestSellerFoundProtoSerde
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import io.koosha.foobar.order_request.OrderRequestStateChangedProtoSerde
import io.koosha.foobar.product.AvailabilityProto
import io.koosha.foobar.product.AvailabilityProtoSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import java.util.UUID

@Configuration
@EnableKafka
@Profile(PROFILE__KAFKA)
class KafkaConfig {

    companion object {
        private const val CONCURRENCY = 4
        private const val POLL_TIMEOUT_MILLIS = 3000L
    }

    @Qualifier(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    @Bean(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    fun orderRequestSellerKafkaProducerFactory(
        prop: KafkaProperties,
    ): ProducerFactory<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to UUIDSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to OrderRequestSellerFoundProtoSerde.Ser::class.java,
            ),
        )

    @Qualifier(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__SELLER_FOUND)
    @Bean(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__SELLER_FOUND)
    fun orderRequestSellerKafkaTemplate(
        @Qualifier(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__SELLER_FOUND)
        producerFactory: ProducerFactory<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>,
    ): KafkaTemplate<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaDefinitions.TOPIC__ORDER_REQUEST__SELLER_FOUND
        }

    @Qualifier(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__AVAILABILITY)
    @Bean(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__AVAILABILITY)
    fun availabilityKafkaListenerContainerFactory(
        @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__AVAILABILITY)
        consumerFactory: ConsumerFactory<UUID, AvailabilityProto.Availability>,
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<UUID, AvailabilityProto.Availability>> {
        return ConcurrentKafkaListenerContainerFactory<UUID, AvailabilityProto.Availability>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(CONCURRENCY)
            this.containerProperties.pollTimeout = POLL_TIMEOUT_MILLIS
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }

    @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__AVAILABILITY)
    @Bean(KafkaDefinitions.CONSUMER_FACTORY__AVAILABILITY)
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

    @Qualifier(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    @Bean(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    fun orderRequestStateChangedKafkaListenerContainerFactory(
        @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
        consumerFactory: ConsumerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,
    ): KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>> =
        ConcurrentKafkaListenerContainerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(CONCURRENCY)
            this.containerProperties.pollTimeout = POLL_TIMEOUT_MILLIS
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }

    @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    @Bean(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    fun orderRequestStateChangedKafkaListenerConsumerFactory(
        prop: KafkaProperties,
    ): ConsumerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to OrderRequestStateChangedProtoSerde.Deser::class.java,
            ),
        )

    @Qualifier(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    @Bean(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    fun orderRequestStateChangedDeadLetterKafkaProducerFactory(
        prop: KafkaProperties,
    ): ProducerFactory<UUID, DeadLetterErrorProto.DeadLetterError> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to UUIDSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to DeadLetterErrorProtoSerde.Ser::class.java,
            ),
        )

    @Qualifier(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    @Bean(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    fun orderRequestStateChangedDeadLetterKafkaTemplate(
        @Qualifier(KafkaDefinitions.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
        producerFactory: ProducerFactory<UUID, DeadLetterErrorProto.DeadLetterError>,
    ): KafkaTemplate<UUID, DeadLetterErrorProto.DeadLetterError> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaDefinitions.TOPIC__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER
        }

}
