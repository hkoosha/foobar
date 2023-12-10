package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.dto.OrderRequestUpdateRequestDto
import io.koosha.foobar.marketplace.api.model.entity.ProcessedOrderRequestSellerDO
import io.koosha.foobar.marketplace.api.model.repo.ProcessedOrderRequestSellerRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Clock
import java.time.Duration
import java.util.UUID

@Component
class OrderRequestSellerProcessor(
    meterRegistry: MeterRegistry,
    private val clock: Clock,
    private val processedRepo: ProcessedOrderRequestSellerRepository,
    private val orderRequestService: OrderRequestService,
) : ConsumerSeekAware {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val sellerFound: Counter = meterRegistry.counter("seller_found", TAG, TAG_VALUE)
    private val sellerNotFound: Counter = meterRegistry.counter("seller_not_found", TAG, TAG_VALUE)

    init {
        log.info("listening to: {}", KafkaDefinitions.TOPIC__ORDER_REQUEST__SELLER_FOUND)
    }

    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback,
    ) =
        assignments.forEach { (topicPartition, _) ->
            callback.seekToBeginning(topicPartition.topic(), topicPartition.partition())
        }

    // TODO switch to RX
    @KafkaListener(
        groupId = "marketplace__order_request_seller",
        concurrency = "2",
        topics = [KafkaDefinitions.TOPIC__ORDER_REQUEST__SELLER_FOUND],
        containerFactory = KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__SELLER_FOUND,
    )
    fun onOrderRequestSeller(
        @Header(KafkaHeaders.RECEIVED_KEY)
        orderRequestId: UUID,

        @Payload
        payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,

        ack: Acknowledgment,
    ) = try {
        this.onOrderRequestSeller0(orderRequestId, payload, ack)
    }
    catch (e: Exception) {
        log.error("WTF??? ============> ", e)
        ack.nack(Duration.ofSeconds(3))
    }

    fun onOrderRequestSeller0(
        orderRequestId: UUID,
        payload: OrderRequestSellerFoundProto.OrderRequestSellerFound,
        ack: Acknowledgment,
    ) {
        val now = this.clock.instant()
        val isNewRecord = this.processedRepo
            .save(
                ProcessedOrderRequestSellerDO(
                    orderRequestId = orderRequestId.toString(),
                    version = null,
                    created = now,
                    updated = now,
                )
            )
            .onErrorResume(this::isDuplicateEntry) {
                Mono.empty()
            }
            .block()

        if (isNewRecord == null) {
            ack.acknowledge()
            return
        }

        val update0 = if (payload.hasSellerId()) {
            sellerFound.increment()
            OrderRequestUpdateRequestDto(
                sellerId = payload.sellerId.toUUID(),
                subTotal = payload.subTotal,
            )
        }
        else {
            sellerNotFound.increment()
            OrderRequestUpdateRequestDto(state = OrderRequestState.NO_SELLER_FOUND)
        }

        val update1 = if (payload.hasSellerId()) {
            OrderRequestUpdateRequestDto(state = OrderRequestState.FULFILLED)
        }
        else {
            null
        }

        this.orderRequestService
            .update(orderRequestId, update0)
            .flatMap {
                if (update1 != null)
                    this.orderRequestService.update(
                        orderRequestId,
                        update1,
                    )
                else
                    Mono.empty<Void>()
            }
            .block()

        ack.acknowledge()
    }


    private fun isDuplicateEntry(it: Throwable): Boolean {
        if (it !is DataIntegrityViolationException)
            return false

        return it.cause?.message?.contains("Duplicate entry") == true ||
                it.message?.contains("Duplicate entry") == true ||
                it.cause?.message?.contains("duplicate key value violates unique constraint") == true ||
                it.message?.contains("duplicate key value violates unique constraint") == true
    }

}
