package io.koosha.foobar.marketplace_engine.api.service

import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.common.cfg.KafkaConfig
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.entity.DeadLetterErrorProto
import io.koosha.foobar.entity.EntityHelper
import io.koosha.foobar.marketplace_engine.SOURCE
import io.koosha.foobar.marketplace_engine.api.model.AvailabilityRepository
import io.koosha.foobar.marketplace_engine.api.model.ProcessedOrderRequestDO
import io.koosha.foobar.marketplace_engine.api.model.ProcessedOrderRequestRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import mu.KotlinLogging
import org.apache.kafka.common.TopicPartition
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.*
import java.util.stream.Collectors


@Component
class OrderRequestProcessor(
    private val clock: Clock,
    private val availabilityRepo: AvailabilityRepository,
    private val processedRepo: ProcessedOrderRequestRepository,
    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    private val deadLetter: KafkaTemplate<UUID, DeadLetterErrorProto.DeadLetterError>,
    @Qualifier(KafkaConfig.TEMPLATE__ORDER_REQUEST__SELLER_FOUND)
    private val kafka: KafkaTemplate<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>,
) : ConsumerSeekAware {

    private val log = KotlinLogging.logger {}

    @KafkaListener(
        groupId = "${SOURCE}__state_change",
        concurrency = "2",
        topics = [KafkaConfig.TOPIC__ORDER_REQUEST__STATE_CHANGED],
        containerFactory = KafkaConfig.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED,
    )
    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    fun onEntityStateChanged(
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: UUID,
        @Payload stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
        ack: Acknowledgment,
    ) {

        this.onEntityStateChanged0(key, stateChange)
        ack.acknowledge()
    }

    private fun onEntityStateChanged0(
        orderRequestId: UUID,
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
    ) {

        if (stateChange.to != "LIVE")
            return

        val already = this.processedRepo.findById(orderRequestId)
        if (already.isPresent && already.get().processed == true) {
            log.debug { "record already processed, skipping. orderRequestId=$orderRequestId" }
            return
        }

        log.trace { "going live, orderRequestId=$orderRequestId" }

        val lineItems: List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem> = stateChange.lineItemsList
        if (lineItems.isEmpty()) {
            log.warn { "order request has no line item, sending to dead letter queue. orderRequestId=$orderRequestId" }
            this.storeUUIDAsProcessed(orderRequestId)
            this.deadLetter.sendDefault(
                orderRequestId,
                EntityHelper.deadLetterErrorOf(
                    SOURCE,
                    this.clock.millis(),
                    "order request has no line item"
                ),
            )
            return
        }

        val productToSellers: Map<UUID, List<UUID>> =
            try {
                lineItems
                    .stream()
                    .collect(
                        Collectors.toUnmodifiableMap(
                            { lineItem -> lineItem.productId.toUUID() },
                            { lineItem ->
                                this.availabilityRepo.findAllByProductIdAndUnitsAvailableGreaterThanEqual(
                                    lineItem.productId.toUUID(),
                                    lineItem.units,
                                ).map { availability -> availability.availabilityPk.sellerId!! }
                            }
                        )
                    )
            }
            catch (ex: IllegalStateException) {
                if (ex.message?.startsWith("Duplicate key ") == true
                    && ex.message?.contains("(attempted merging values") == true
                ) {
                    log.warn {
                        "order request has duplicated line item, sending to dead letter queue. " +
                                "orderRequestId=$orderRequestId lineItemsSize=${lineItems.size}"
                    }
                    this.storeUUIDAsProcessed(orderRequestId)
                    this.deadLetter.sendDefault(
                        orderRequestId,
                        EntityHelper.deadLetterErrorOf(
                            SOURCE,
                            this.clock.millis(),
                            "order request has duplicated line item"
                        ),
                    )
                    return
                }
                else
                    throw ex
            }

        val sellers: List<UUID> = productToSellers.values.flatten()

        // A seller who has availability for all line items
        var luckySeller: UUID? = null
        next_seller@ for (seller in sellers) {
            for ((_, productSeller) in productToSellers)
                if (!productSeller.contains(seller))
                    continue@next_seller
            luckySeller = seller
            break
        }

        var subTotal = 0L
        if (luckySeller != null)
            for (lineItem in lineItems) {
                val availability =
                    this.availabilityRepo.findAllByproductIdAndSellerId(lineItem.productId.toUUID(), luckySeller).get()
                availability.unitsAvailable = availability.unitsAvailable!! - lineItem.units
                subTotal += availability.pricePerUnit!! * lineItem.units
                this.availabilityRepo.save(availability)
            }

        val send = OrderRequestSellerFoundProto.OrderRequestSellerFound
            .newBuilder()
            .setHeader(HeaderHelper.create(SOURCE, this.clock.millis()))

        if (luckySeller == null) {
            log.info { "no seller found, orderRequestId=$orderRequestId" }
            send.clearSellerId()
            send.clearSubTotal()
        }
        else {
            log.info { "seller found, orderRequestId=$orderRequestId, sellerId=$luckySeller" }
            send.sellerId = luckySeller.toString()
            send.subTotal = subTotal
        }

        log.trace { "sending back seller. orderRequestId=$orderRequestId, sellerId=$luckySeller" }
        this.kafka.sendDefault(
            orderRequestId,
            send.build(),
        )
        this.storeUUIDAsProcessed(orderRequestId)
    }

    private fun storeUUIDAsProcessed(orderRequestId: UUID) =
        this.processedRepo.save(
            ProcessedOrderRequestDO(
                orderRequestId = orderRequestId,
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
