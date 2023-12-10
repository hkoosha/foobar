package io.koosha.foobar.shipping.api.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.order_request.OrderRequestSellerFoundProtoSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
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

    @Qualifier(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    @Bean(KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    fun orderRequestSellerKafkaListenerContainerFactory(
        @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__SELLER_FOUND)
        consumerFactory: ConsumerFactory<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>,
    ): KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>> =
        ConcurrentKafkaListenerContainerFactory<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>().apply {
            this.consumerFactory = consumerFactory
            this.setConcurrency(CONCURRENCY)
            this.containerProperties.pollTimeout = POLL_TIMEOUT_MILLIS
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }

    @Qualifier(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    @Bean(KafkaDefinitions.CONSUMER_FACTORY__ORDER_REQUEST__SELLER_FOUND)
    fun orderRequestSellerKafkaListenerConsumerFactory(
        prop: KafkaProperties,
    ): ConsumerFactory<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to prop.bootstrapServers.joinToString(","),
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to UUIDDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to OrderRequestSellerFoundProtoSerde.Deser::class.java,
            ),
        )

}
