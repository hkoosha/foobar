package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.entity.DeadLetterErrorProto
import io.koosha.foobar.entity.DeadLetterErrorProtoSerde
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import io.koosha.foobar.order_request.OrderRequestStateChangedProtoSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
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
class MarketplaceKafkaConfig {

    @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    @Lazy
    @Bean(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    fun orderRequestStateChangedKafkaProducerFactory(
        prop: KafkaProperties,
    ): ProducerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to UUIDSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to OrderRequestStateChangedProtoSerde.Ser::class.java,
            ),
        )

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    @Lazy
    @Bean(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED)
    fun orderRequestStateChangedKafkaTemplate(
        @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED)
        producerFactory: ProducerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,
    ): KafkaTemplate<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaConfig.TOPIC__ORDER_REQUEST__STATE_CHANGED
        }

    @Qualifier(KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    @Lazy
    @Bean(KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    fun orderRequestStateChangedKafkaListenerContainerFactory(
        @Qualifier(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
        consumerFactory: ConsumerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>,
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>> =
        ConcurrentKafkaListenerContainerFactory<UUID, OrderRequestStateChangedProto.OrderRequestStateChanged>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(4)
            this.containerProperties.pollTimeout = 3000
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }

    @Qualifier(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
    @Lazy
    @Bean(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED)
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

    // ==================================================================

    @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    @Lazy
    @Bean(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
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

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    @Lazy
    @Bean(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    fun orderRequestStateChangedDeadLetterKafkaTemplate(
        @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
        producerFactory: ProducerFactory<UUID, DeadLetterErrorProto.DeadLetterError>,
    ): KafkaTemplate<UUID, DeadLetterErrorProto.DeadLetterError> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaConfig.TOPIC__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER
        }

}
