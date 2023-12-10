package io.koosha.foobar.kafka;

public final class KafkaDefinitions {

    private KafkaDefinitions() {
        throw new AssertionError("can not instantiate utility class");
    }

    public static final String TOPIC__ORDER_REQUEST__STATE_CHANGED =
            "foobar--marketplace--order-request--state-changed";
    public static final String TOPIC__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            TOPIC__ORDER_REQUEST__STATE_CHANGED + "--dead-letter";
    public static final String TOPIC__ORDER_REQUEST__SELLER_FOUND =
            "foobar--marketplace-engine--order-request--seller-found";
    public static final String TOPIC__AVAILABILITY =
            "foobar--warehouse--availability";


    // =====================================================================


    private static final String K__ORDER_REQUEST__STATE_CHANGED =
            "marketplace--order-request--state-changed";

    public static final String TEMPLATE__ORDER_REQUEST__STATE_CHANGED =
            "foobar-kafka-template--" + K__ORDER_REQUEST__STATE_CHANGED;

    public static final String PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar-kafka-producer-factory--" + K__ORDER_REQUEST__STATE_CHANGED;

    public static final String LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar-kafka-listener-container-factory--" + K__ORDER_REQUEST__STATE_CHANGED;

    public static final String CONSUMER_FACTORY__ORDER_REQUEST__STATE_CHANGED =
            "foobar-kafka-consumer-factory--" + K__ORDER_REQUEST__STATE_CHANGED;


    public static final String TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            TEMPLATE__ORDER_REQUEST__STATE_CHANGED + "--dead-letter";

    public static final String PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER =
            PROD_FACTORY__ORDER_REQUEST__STATE_CHANGED + "--dead-letter";


    // =====================================================================


    private static final String K__ORDER_REQUEST__SELLER_FOUND =
            "marketplace-engine--order-request--seller-found";

    public static final String TEMPLATE__ORDER_REQUEST__SELLER_FOUND =
            "foobar-kafka-template--" + K__ORDER_REQUEST__SELLER_FOUND;

    public static final String PROD_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar-kafka-producer-factory--" + K__ORDER_REQUEST__SELLER_FOUND;

    public static final String LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar-kafka-listener-container-factory--" + K__ORDER_REQUEST__SELLER_FOUND;

    public static final String CONSUMER_FACTORY__ORDER_REQUEST__SELLER_FOUND =
            "foobar-kafka-consumer-factory--" + K__ORDER_REQUEST__SELLER_FOUND;


    // =====================================================================


    private static final String K__AVAILABILITY =
            "warehouse--availability";

    public static final String TEMPLATE__AVAILABILITY =
            "foobar-kafka-template--" + K__AVAILABILITY;

    public static final String PROD_FACTORY__AVAILABILITY =
            "foobar-kafka-producer-factory--" + K__AVAILABILITY;

    public static final String LISTENER_CONTAINER_FACTORY__AVAILABILITY =
            "foobar-kafka-listener-container-factory--" + K__AVAILABILITY;

    public static final String CONSUMER_FACTORY__AVAILABILITY =
            "foobar-kafka-consumer-container-factory--" + K__AVAILABILITY;

}
