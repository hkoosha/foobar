package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestDO
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerDO
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import mu.KotlinLogging
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Component
class OrderRequestSellerProcessor(
    private val processedRepo: ProcessedOrderRequestSellerRepository,
    private val orderRequestService: OrderRequestService,
) : ConsumerSeekAware {

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

        val already = this.processedRepo.findById(orderRequestId)
        if (already.isPresent && already.get().processed == true) {
            log.debug { "record already processed, skipping. orderRequestId=$orderRequestId" }
            return
        }

        if (payload.hasSellerId()) {
            this.orderRequestService.update(
                orderRequestId,
                OrderRequestUpdateRequest(
                    sellerId = payload.sellerId.toUUID(),
                    subTotal = payload.subTotal,
                )
            )
            this.orderRequestService.update(
                orderRequestId,
                OrderRequestUpdateRequest(
                    state = OrderRequestState.FULFILLED,
                )
            )
        }
        else {
            this.orderRequestService.update(
                orderRequestId,
                OrderRequestUpdateRequest(
                    state = OrderRequestState.NO_SELLER_FOUND,
                )
            )
        }

        val orderRequest = this.orderRequestService.findById(orderRequestId).get()
        this.storeUUIDAsProcessed(orderRequest)
    }

    private fun storeUUIDAsProcessed(orderRequest: OrderRequestDO) =
        this.processedRepo.save(
            ProcessedOrderRequestSellerDO(
                processedOrderRequestSellerId = orderRequest.orderRequestId,
                orderRequest = orderRequest,
                version = null,
                created = null,
                updated = null,
                processed = true
            )
        )

    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback,
    ) = assignments.forEach { (topicPartition, _) ->
        callback.seekToBeginning(topicPartition.topic(), topicPartition.partition())
    }

}