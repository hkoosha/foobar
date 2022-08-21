package io.koosha.foobar.shipping.api.service

import feign.codec.EncodeException
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.shipping.SOURCE
import mu.KotlinLogging
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
            log.trace { "order request has no seller: $orderRequestId" }
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
        catch (ex: EncodeException) {
            log.error(ex) {
                "failed to process shipping for order request, payload=$payload, orderRequestId=$orderRequestId"
            }
        }
    }

}