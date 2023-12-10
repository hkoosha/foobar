package io.koosha.foobar.marketplaceengine.api.service

import io.koosha.foobar.HeaderHelper
import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.common.toUUID
import io.koosha.foobar.entity.DeadLetterErrorProto
import io.koosha.foobar.entity.EntityHelper
import io.koosha.foobar.kafka.KafkaDefinitions
import io.koosha.foobar.marketplaceengine.api.model.entity.ProcessedOrderRequestDO
import io.koosha.foobar.marketplaceengine.api.model.repo.AvailabilityRepository
import io.koosha.foobar.marketplaceengine.api.model.repo.ProcessedOrderRequestRepository
import io.koosha.foobar.order_request.OrderRequestSellerFoundProto
import io.koosha.foobar.order_request.OrderRequestStateChangedProto
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import net.logstash.logback.argument.StructuredArguments.v
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.time.Clock
import java.time.Duration
import java.util.Optional
import java.util.UUID
import java.util.concurrent.TimeUnit

// FIXME fix tx mess
@Component
class OrderRequestProcessor(
    meterRegistry: MeterRegistry,
    private val clock: Clock,
    private val availabilityRepo: AvailabilityRepository,
    private val processedRepo: ProcessedOrderRequestRepository,
    private val txTemplate: TransactionTemplate,

    @Qualifier(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__STATE_CHANGED__DEAD_LETTER)
    private val deadLetter: KafkaTemplate<UUID, DeadLetterErrorProto.DeadLetterError>,

    @Qualifier(KafkaDefinitions.TEMPLATE__ORDER_REQUEST__SELLER_FOUND)
    private val kafka: KafkaTemplate<UUID, OrderRequestSellerFoundProto.OrderRequestSellerFound>,
) : ConsumerSeekAware {

    companion object {
        private const val KAFKA_TIMEOUT_MILLIS = 3000L
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    private val timer: Timer = meterRegistry.timer("onEntityStateChanged", TAG, TAG_VALUE)

    init {
        log.info("sending to topic: {}", this.kafka.defaultTopic)
        log.info("sending to topic: {}", this.deadLetter.defaultTopic)
        log.info("listening to: {}", KafkaDefinitions.TOPIC__ORDER_REQUEST__STATE_CHANGED)
    }

    @KafkaListener(
        groupId = "marketplace_engine__state_change",
        concurrency = "2",
        topics = [KafkaDefinitions.TOPIC__ORDER_REQUEST__STATE_CHANGED],
        containerFactory = KafkaDefinitions.LISTENER_CONTAINER_FACTORY__ORDER_REQUEST__STATE_CHANGED,
    )
    fun onEntityStateChanged(
        @Header(KafkaHeaders.RECEIVED_KEY)
        key: UUID,

        @Payload
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,

        ack: Acknowledgment,
    ) {

        if (stateChange.to == "LIVE")
            timer.record(Runnable {
                try {
                    this.txTemplate.execute {
                        this.onEntityStateChanged0(key, stateChange)
                    }
                    ack.acknowledge()
                }
                catch (@Suppress("SwallowedException") e: SkipProcessing) {
                    ack.acknowledge()
                }
                catch (@Suppress("SwallowedException") e: ObjectOptimisticLockingFailureException) {
                    ack.acknowledge()
                }
                catch (e: Exception) {
                    log.error("failed to process entity state change -> {} -> {}", e::class.java, e.message)
                    ack.nack(Duration.ofMillis(KAFKA_TIMEOUT_MILLIS))
                }
            })
    }

    @Suppress("ReturnCount")
    private fun onEntityStateChanged0(
        orderRequestId: UUID,
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
    ) {

        check(stateChange.to == "LIVE")

        val already: Optional<ProcessedOrderRequestDO> = this.processedRepo.findById(orderRequestId)
        if (already.isPresent && already.get().processed == true) {
            log.trace(
                "record already processed, skipping. orderRequestId={}",
                v("orderRequestId", orderRequestId),
            )
            throw SkipProcessing
        }

        log.trace("going live, orderRequestId={}", v("orderRequestId", orderRequestId))

        val lineItems: List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem> =
            this.getLineItemsOf(orderRequestId, stateChange) ?: return
        val productToSellers = this.getProductToSellers(orderRequestId, lineItems) ?: return
        val sellers: List<UUID> = productToSellers.values.flatten()
        val luckySeller: UUID? = this.findLuckySeller(sellers, productToSellers)
        val subTotal = this.getSubTotal(luckySeller, lineItems)

        val send = OrderRequestSellerFoundProto.OrderRequestSellerFound
            .newBuilder()
            .setHeader(HeaderHelper.create("marketplace_engine", this.clock.millis()))

        if (luckySeller == null) {
            log.info("no seller found, orderRequestId={}", v("orderRequestId", orderRequestId))
            send.clearSellerId()
            send.clearSubTotal()
        }
        else {
            log.info(
                "seller found, orderRequestId={}, sellerId={}",
                v("orderRequestId", orderRequestId),
                v("sellerId", luckySeller),
            )
            send.sellerId = luckySeller.toString()
            send.subTotal = subTotal
        }

        log.trace(
            "sending back seller. orderRequestId={}, sellerId={}",
            v("orderRequestId", orderRequestId),
            v("sellerId", luckySeller),
        )
        this.kafka.sendDefault(
            orderRequestId,
            send.build(),
        ).get(KAFKA_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)

        this.storeUUIDAsProcessed(orderRequestId)
    }

    private fun getLineItemsOf(
        orderRequestId: UUID,
        stateChange: OrderRequestStateChangedProto.OrderRequestStateChanged,
    ): List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem>? {

        val lineItems: List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem> = stateChange.lineItemsList

        return lineItems.ifEmpty {
            log.warn(
                "order request has no line item, sending to dead letter queue. orderRequestId={}",
                v("orderRequestId", orderRequestId),
            )

            this.storeUUIDAsProcessed(orderRequestId)

            this.deadLetter.sendDefault(
                orderRequestId,
                EntityHelper.deadLetterErrorOf(
                    "marketplace_engine",
                    this.clock.millis(),
                    "order request has no line item"
                ),
            )

            null
        }
    }

    private fun getProductToSellers(
        orderRequestId: UUID,
        lineItems: List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem>,
    ): Map<UUID, List<UUID>>? {

        val productToSellers: Map<UUID, List<UUID>> =
            try {
                lineItems
                    .asSequence()
                    .map {
                        val productId: UUID = it.productId.toUUID()

                        val availabilities: List<UUID> = this
                            .availabilityRepo
                            .findAllByProductIdAndUnitsAvailableGreaterThanEqual(
                                productId,
                                it.units
                            )
                            .map { a -> a.availabilityPk.sellerId!! }

                        productId to availabilities
                    }
                    .toMap()
            }
            catch (ex: IllegalStateException) {
                if (ex.message?.startsWith("Duplicate key ") == true
                    && ex.message?.contains("(attempted merging values") == true
                ) {
                    log.warn(
                        "order request has duplicated line item, sending to dead letter queue. " +
                                "orderRequestId={}, lineItemsSize={}",
                        v("orderRequestId", orderRequestId),
                        v("lineItemsSize", lineItems.size),
                    )
                    this.storeUUIDAsProcessed(orderRequestId)
                    this.deadLetter.sendDefault(
                        orderRequestId,
                        EntityHelper.deadLetterErrorOf(
                            "marketplace_engine",
                            this.clock.millis(),
                            "order request has duplicated line item"
                        ),
                    )
                    return null
                }
                else
                    throw ex
            }

        return productToSellers
    }

    private fun findLuckySeller(
        sellers: List<UUID>,
        productToSellers: Map<UUID, List<UUID>>,
    ): UUID? {

        return sellers
            .asSequence()
            .filter { seller ->
                // A seller who has availability for all requested line items in the order.
                for ((_, productSeller) in productToSellers)
                    if (!productSeller.contains(seller))
                        return@filter false

                true
            }
            .toList()
            .randomOrNull()
    }

    private fun getSubTotal(
        luckySeller: UUID?,
        lineItems: List<OrderRequestStateChangedProto.OrderRequestStateChanged.LineItem>,
    ): Long {

        var subTotal = 0L

        if (luckySeller != null)
            for (lineItem in lineItems) {
                val availability =
                    this.availabilityRepo.findByproductIdAndSellerId(lineItem.productId.toUUID(), luckySeller).get()
                availability.unitsAvailable = availability.unitsAvailable!! - lineItem.units
                subTotal += availability.pricePerUnit!! * lineItem.units
                this.availabilityRepo.save(availability)
            }

        return subTotal
    }

    private fun storeUUIDAsProcessed(orderRequestId: UUID) =
        try {
            this.processedRepo.save(
                ProcessedOrderRequestDO(
                    orderRequestId = orderRequestId,
                    version = null,
                    created = null,
                    updated = null,
                    processed = true
                )
            )
        }
        catch (e: DataIntegrityViolationException) {
            if (this.isDuplicateEntry(e))
                throw SkipProcessing
            else
                throw e
        }

    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback,
    ) = assignments.forEach { (topicPartition, _) ->
        callback.seekToBeginning(topicPartition.topic(), topicPartition.partition())
    }

    private fun isDuplicateEntry(
        it: Throwable,
    ): Boolean {

        if (it !is DataIntegrityViolationException)
            return false

        return it.cause?.message?.contains("Duplicate entry") == true ||
                it.message?.contains("Duplicate entry") == true ||
                it.cause?.message?.contains("duplicate key value violates unique constraint") == true ||
                it.message?.contains("duplicate key value violates unique constraint") == true
    }

    private object SkipProcessing : RuntimeException() {
        private fun readResolve(): Any = SkipProcessing
    }

}
