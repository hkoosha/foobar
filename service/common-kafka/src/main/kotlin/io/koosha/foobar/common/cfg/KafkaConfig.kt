package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka


@Suppress("UtilityClassWithPublicConstructor")
@Configuration
@EnableKafka
@Profile(PROFILE__KAFKA)
class KafkaConfig {

    companion object {

        const val TOPIC__ORDER_REQUEST__STATE_CHANGED = "foobar__marketplace__order_request__state_changed"
        const val TOPIC__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            "${TOPIC__ORDER_REQUEST__STATE_CHANGED}__dead_letter"
        const val TOPIC__ORDER_REQUEST__SELLER_FOUND = "foobar__marketplace_engine__order_request__seller_found"
        const val TOPIC__AVAILABILITY = "foobar__warehouse__availability"


        // =====================================================================


        private const val K__ORDER_REQUEST__STATE_CHANGED =
            "marketplace__order_request__state_changed"

        const val TEMPLATE__ORDER_REQUEST__STATE_CHANGED =
            "foobar_kafka_template__$K__ORDER_REQUEST__STATE_CHANGED"

        const val PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar_kafka_producer_factory__$K__ORDER_REQUEST__STATE_CHANGED"

        const val LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar_kafka_listener_container_factory__$K__ORDER_REQUEST__STATE_CHANGED"

        const val CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar_kafka_consumer_factory__$K__ORDER_REQUEST__STATE_CHANGED"


        const val TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            "${TEMPLATE__ORDER_REQUEST__STATE_CHANGED}__dead_letter"

        const val PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            "${PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED}__dead_letter"


        // =====================================================================


        private const val K__ORDER_REQUEST__SELLER_FOUND =
            "marketplace_engine__order_request__seller_found"

        const val TEMPLATE__ORDER_REQUEST__SELLER_FOUND =
            "foobar_kafka_template__$K__ORDER_REQUEST__SELLER_FOUND"

        const val PROD_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar_kafka_producer_factory__$K__ORDER_REQUEST__SELLER_FOUND"

        const val LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar_kafka_listener_container_factory__$K__ORDER_REQUEST__SELLER_FOUND"

        const val CONSUMER_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar_kafka_consumer_factory__$K__ORDER_REQUEST__SELLER_FOUND"


        // =====================================================================


        private const val K__AVAILABILITY =
            "warehouse__availability"

        const val TEMPLATE__AVAILABILITY =
            "foobar_kafka_template__$K__AVAILABILITY"

        const val PROD_FACTORY__AVAILABILITY =
            "foobar_kafka_producer_factory__$K__AVAILABILITY"

        const val LISTENER_CONTAINER_FACTORY__AVAILABILITY =
            "foobar_kafka_listener_container_factory__$K__AVAILABILITY"

        const val CONSUMER_FACTORY__AVAILABILITY =
            "foobar_kafka_consumer_container_factory__$K__AVAILABILITY"

    }

}
