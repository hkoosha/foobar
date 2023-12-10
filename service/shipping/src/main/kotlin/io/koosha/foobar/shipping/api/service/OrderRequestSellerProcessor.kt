package io.koosha.foobar.shipping.api.service

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.shipping.api.model.dto.ShippingCreateRequestDto
import io.micrometer.core.annotation.Timed
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Component
class OrderRequestSellerProcessor(
    private val shippingService: ShippingService,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        log.info("listening to: {}", KafkaDefinitions.TOPIC__ORDER_REQUEST__SELLER_FOUND)
    }

    @Timed(extraTags = [TAG, TAG_VALUE])
    @KafkaListener(
        groupId = "shipping__order_request_seller",
        concurrency = "2",
        topics = [KafkaDefinitions.TOPIC__ORDER_REQUEST__SELLER_FOUND],
        containerFactory = KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND,
    )
    @Transactional(
        rollbackFor = [Exception::class],
    )
    fun onOrderRequestSeller(
        @Header(KafkaHeaders.RECEIVED_KEY)
        key: UUID,

        @Payload
        payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,

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
                ShippingCreateRequestDto(
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
