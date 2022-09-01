package io.koosha.foobar.shipping.api.service

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.shipping.SOURCE
import io.micrometer.core.annotation.Timed
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Component
class OrderRequestSellerProcessor(
    private val shippingService: ShippingService,
) {

    private val log = KotlinLogging.logger {}

    @Timed(extraTags = [TAG, TAG_VALUE])
    @KafkaListener(
        groupId = "${SOURCE}__order_request_seller",
        concurrency = "2",
        topics = [KafkaConfig.TOPIC__ORDER_REQUEST__SELLER_FOUND],
        containerFactory = KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND,
    )
    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun onOrderRequestSeller(
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: UUID,
        @Payload payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,
        ack: Acknowledgment,
    ) {

        this.onOrderRequestSeller0(key, payload)
        ack.acknowledge()
    }

    private fun onOrderRequestSeller0(
        orderRequestId: UUID,
        payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,
    ) {

        if (!payload.hasSellerId()) {
            log.trace("order request has no seller: {}", v("orderRequestId", orderRequestId))
            return
        }

        try {
            this.shippingService.create(
                ShippingCreateRequest(
                    sellerId = payload.sellerId.toUUID(),
                    orderRequestId = orderRequestId,
                )
            )
        }
        catch (ex: Exception) {
            log.error(
                "failed to process shipping for order request, payload={}, orderRequestId={}",
                v("payload", payload),
                v("orderRequestId", orderRequestId),
                ex,
            )
        }
    }

}
