package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka


@EnableKafka
@Profile(PROFILE__KAFKA)
class KafkaConfig {

    companion object {

        const val TOPIC__ORDER_REQUEST__ENTITY_STATE = "foobar__marketplace__order_request__entity_state"
        const val TOPIC__ORDER_REQUEST__ENTITY_STATE__DEAD_LETTER = "${TOPIC__ORDER_REQUEST__ENTITY_STATE}__dead_letter"
        const val TOPIC__ORDER_REQUEST__SELLER = "foobar__marketplace_engine__order_request__seller"
        const val TOPIC__AVAILABILITY = "foobar__warehouse__availability"


        // =====================================================================


        private const val K__ORDER_REQUEST__ENTITY_STATE =
            "marketplace__order_request__entity_state"

        const val TEMPLATE__ORDER_REQUEST__ENTITY_STATE =
            "foobar_kafka_template__$K__ORDER_REQUEST__ENTITY_STATE"

        const val PROD_FACTORY__ORDER_REQUEST__ENTITY_STATE =
            "foobar_kafka_producer_factory__$K__ORDER_REQUEST__ENTITY_STATE"

        const val LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__ENTITY_STATE =
            "foobar_kafka_listener_container_factory__$K__ORDER_REQUEST__ENTITY_STATE"

        const val CONSUMER_FACTORY__ORDER_REQUEST__ENTITY_STATE =
            "foobar_kafka_consumer_factory__$K__ORDER_REQUEST__ENTITY_STATE"


        const val TEMPLATE__ORDER_REQUEST__ENTITY_STATE__DEAD_LETTER =
            "${TEMPLATE__ORDER_REQUEST__ENTITY_STATE}__dead_letter"
        const val PROD_FACTORY__ORDER_REQUEST__ENTITY_STATE__DEAD_LETTER =
            "${PROD_FACTORY__ORDER_REQUEST__ENTITY_STATE}__dead_letter"


        // =====================================================================


        private const val K__ORDER_REQUEST__SELLER =
            "marketplace_engine__order_request__seller"

        const val TEMPLATE__ORDER_REQUEST__SELLER =
            "foobar_kafka_template__$K__ORDER_REQUEST__SELLER"

        const val PROD_FACTORY__ORDER_REQUEST__SELLER =
            "foobar_kafka_producer_factory__$K__ORDER_REQUEST__SELLER"

        const val LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER =
            "foobar_kafka_listener_container_factory__$K__ORDER_REQUEST__SELLER"

        const val CONSUMER_FACTORY__ORDER_REQUEST__SELLER =
            "foobar_kafka_consumer_factory__$K__ORDER_REQUEST__SELLER"


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
