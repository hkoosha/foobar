package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerDO
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.micrometer.core.annotation.Timed
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.apache.kafka.common.TopicPartition
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*


@Component
class OrderRequestSellerProcessor(
    private val processedRepo: ProcessedOrderRequestSellerRepository,

    private val orderRequestService: OrderRequestService,
) : ConsumerSeekAware {

    private val log = KotlinLogging.logger {}

    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback,
    ) = assignments.forEach { (topicPartition, _) ->
        callback.seekToBeginning(topicPartition.topic(), topicPartition.partition())
    }

    // TODO switch to RX
    @Timed(extraTags = [TAG, TAG_VALUE])
    @KafkaListener(
        groupId = "${SOURCE}__order_request_seller",
        concurrency = "2",
        topics = [KafkaConfig.TOPIC__ORDER_REQUEST__SELLER_FOUND],
        containerFactory = KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND,
    )
    fun onOrderRequestSeller(
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) orderRequestId: UUID,
        @Payload payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,
        ack: Acknowledgment,
    ) {

        log.trace("kafka payload=$payload", v("payload", payload))

        val isNewRecord = this.processedRepo
            .save(
                ProcessedOrderRequestSellerDO(
                    orderRequestId = orderRequestId.toString(),
                    version = null,
                    created = null,
                    updated = null,
                )
            )
            .onErrorResume({
                if (it is DataIntegrityViolationException
                    && it.cause?.message?.contains("Duplicate entry") == true
                ) {
                    log.trace(
                        "record already processed, skipping, orderRequestId={}",
                        v("orderRequestId", orderRequestId),
                    )
                    true
                }
                else {
                    false
                }
            }) {
                Mono.empty()
            }
            .block()

        if (isNewRecord == null) {
            ack.acknowledge()
            return
        }

        if (payload.hasSellerId()) {
            this.orderRequestService
                .update(
                    orderRequestId,
                    OrderRequestUpdateRequest(
                        sellerId = payload.sellerId.toUUID(),
                        subTotal = payload.subTotal,
                    )
                )
                .flatMap {
                    this.orderRequestService.update(
                        orderRequestId,
                        OrderRequestUpdateRequest(
                            state = OrderRequestState.FULFILLED,
                        )
                    )
                }
                .block()
        }
        else {
            this.orderRequestService
                .update(
                    orderRequestId,
                    OrderRequestUpdateRequest(
                        state = OrderRequestState.NO_SELLER_FOUND,
                    )
                )
                .block()
        }

        ack.acknowledge()
    }

}
