package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.marketplace.SOURCE
import io.koosha.foobar.marketplace.api.model.OrderRequestState
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerDO
import io.koosha.foobar.marketplace.api.model.ProcessedOrderRequestSellerRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.apache.kafka.common.TopicPartition
import org.springframework.dao.DuplicateKeyException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.*


@Component
class OrderRequestSellerProcessor(
    private val processedRepo: ProcessedOrderRequestSellerRepository,

    private val orderRequestService: OrderRequestService,
) : ConsumerSeekAware {

    companion object {
        const val KAFKA_TIMEOUT_MILLIS = 3000L
    }

    private val log = KotlinLogging.logger {}


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

        this.processedRepo
            .save(
                ProcessedOrderRequestSellerDO(
                    orderRequestId = orderRequestId.toString(),
                    version = null,
                    created = null,
                    updated = null,
                )
            )
            .onErrorResume({ it !is DuplicateKeyException }) {
                Mono.empty()
            }
            .doOnError {
                if (it is DuplicateKeyException) {
                    log.trace(
                        "record already processed, skipping, orderRequestId={}, payload={}",
                        v("orderRequestId", orderRequestId),
                        v("payload", payload)
                    )
                    ack.acknowledge()
                }
                else {
                    log.error(
                        "could not check if record is already processed, orderRequestId={}, payload={}",
                        v("orderRequestId", orderRequestId),
                        v("payload", payload),
                        it
                    )
                    ack.nack(Duration.ofMillis(KAFKA_TIMEOUT_MILLIS))
                }
            }
            .switchIfEmpty(
                Mono.defer {
                    if (payload.hasSellerId()) {
                        this.orderRequestService.update(
                            orderRequestId,
                            OrderRequestUpdateRequest(
                                sellerId = payload.sellerId.toUUID(),
                                subTotal = payload.subTotal,
                            )
                        ).flatMap {
                            this.orderRequestService.update(
                                orderRequestId,
                                OrderRequestUpdateRequest(
                                    state = OrderRequestState.FULFILLED,
                                )
                            )
                        }
                    }
                    else {
                        this.orderRequestService.update(
                            orderRequestId,
                            OrderRequestUpdateRequest(
                                state = OrderRequestState.NO_SELLER_FOUND,
                            )
                        )
                    }
                }
                    .flatMap {
                        this.processedRepo.save(
                            ProcessedOrderRequestSellerDO(
                                orderRequestId = orderRequestId.toString(),
                                version = null,
                                created = null,
                                updated = null,
                            )
                        )
                    }
                    .flatMap {
                        ack.acknowledge()
                        Mono.empty()
                    })
            .subscribeOn(Schedulers.immediate())
            .block()

    }

    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback,
    ) = assignments.forEach { (topicPartition, _) ->
        callback.seekToBeginning(topicPartition.topic(), topicPartition.partition())
    }

}
