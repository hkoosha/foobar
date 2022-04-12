package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.order_request.OrderRequestSellerProto
import io.koosha.foobar.order_request.OrderRequestSellerProtoSerde
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
class MarketplaceEngineKafkaConfig {

    @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__SELLER)
    @Lazy
    @Bean(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__SELLER)
    fun orderRequestSellerKafkaProducerFactory(
        prop: KafkaProperties,
    ): ProducerFactory<UUID, OrderRequestSellerProto.OrderRequestSeller> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to UUIDSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to OrderRequestSellerProtoSerde.Ser::class.java,
            ),
        )

    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__SELLER)
    @Lazy
    @Bean(KafkaConfig.TEMPLATE__ORDER_REQUEST__SELLER)
    fun orderRequestSellerKafkaTemplate(
        @Qualifier(KafkaConfig.PROD_FACTORY__ORDER_REQUEST__SELLER)
        producerFactory: ProducerFactory<UUID, OrderRequestSellerProto.OrderRequestSeller>,
    ): KafkaTemplate<UUID, OrderRequestSellerProto.OrderRequestSeller> =
        KafkaTemplate(producerFactory).apply {
            this.defaultTopic = KafkaConfig.TOPIC__ORDER_REQUEST__SELLER
        }

    @Qualifier(KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER)
    @Lazy
    @Bean(KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER)
    fun orderRequestSellerKafkaListenerContainerFactory(
        @Qualifier(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__SELLER)
        consumerFactory: ConsumerFactory<UUID, OrderRequestSellerProto.OrderRequestSeller>,
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<UUID, OrderRequestSellerProto.OrderRequestSeller>> =
        ConcurrentKafkaListenerContainerFactory<UUID, OrderRequestSellerProto.OrderRequestSeller>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(4)
            this.containerProperties.pollTimeout = 3000
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }

    @Qualifier(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__SELLER)
    @Lazy
    @Bean(KafkaConfig.CONSUMER_FACTORY__ORDER_REQUEST__SELLER)
    fun orderRequestSellerKafkaListenerContainerFactory(
        prop: KafkaProperties,
    ): ConsumerFactory<UUID, OrderRequestSellerProto.OrderRequestSeller> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to OrderRequestSellerProtoSerde.Deser::class.java,
            ),
        )

}
