package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@Component
@Suppress("MagicNumber")
class LoopCmd(
    private val meterRegistry: MeterRegistry,

    private val entityIdService: EntityIdService,

    private val customerCmd: CustomerCmd,
    private val addressCmd: AddressCmd,
    private val sellerCmd: SellerCmd,
    private val availabilityCmd: AvailabilityCmd,
    private val productCmd: ProductCmd,
    private val orderRequestCmd: OrderRequestCmd,
    private val lineItemCmd: LineItemCmd,
) : Command {

    companion object {
        // TODO remove this, no seller will have all products. Only 1 works.
        private const val PROD_COUNT = 1
        private const val UNITS = 3L
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName = "loop"

    // TODO fix suppression.
    @Suppress("LongMethod", "ComplexMethod")
    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val threads = mutableListOf<Thread>()

        for (looper in freeArgs) {
            val (loop, count) = this.extractCount(looper)
            repeat(count) {
                when {
                    // ====================================================== CREATE

                    matches("c:customer", loop) -> threads += Thread {
                        this.runMeasured("c_customer") {
                            this.customerCmd.postCustomer(args, doLog = false)
                        }
                    }

                    matches("c:address", loop) -> threads += Thread {
                        this.runMeasured("c_address") {
                            // TODO limit number of addresses allowed in customer-api.
                            this.addressCmd.postAddress(args, emptyList(), doLog = false)
                        }
                    }

                    matches("c:seller", loop) -> threads += Thread {
                        this.runMeasured("c_seller") {
                            this.sellerCmd.postSeller(args, doLog = false)
                        }
                    }

                    matches("c:product", loop) -> threads += Thread {
                        this.runMeasured("c_product") {
                            this.productCmd.postProduct(args, doLog = false)
                        }
                    }

                    matches("c:availability", loop) -> threads += Thread {
                        this.runMeasured("c_availability") {
                            this.availabilityCmd.postAvailability(args, emptyList(), doLog = false)
                        }
                    }

                    matches("c:order-request", loop) -> threads += Thread {
                        this.runMeasured("c_order_request") {
                            this.orderRequestCmd.postOrderRequest(emptyList(), doLog = false)
                        }
                    }

                    matches("c:line-item", loop) -> threads += Thread {

                        // TODO limit number of line items allowed in warehouse-api.
                        this.runMeasured("c_line_item") {
                            val orderRequestId = this.entityIdService.getOrderRequestFromLineItemWorkQueue()

                            if (orderRequestId.isPresent) {

                                val productIds = mutableSetOf<UUID>()
                                while (productIds.size < PROD_COUNT) {
                                    val availableProduct =
                                        this.entityIdService.getAvailableProduct(UNITS, productIds)
                                    if (availableProduct.isEmpty) {
                                        log.trace(
                                            "no available product to add to line item, so far have: {}",
                                            productIds.size,
                                        )
                                        Thread.sleep(50)
                                    }
                                    else {
                                        productIds += availableProduct.get()
                                    }
                                }

                                for (productId in productIds)
                                    this.lineItemCmd.postLineItem(orderRequestId.get(), productId, UNITS)

                                this.entityIdService.putOrderRequestIntoUpdateWorkQueue(orderRequestId.get())
                            }
                            else {
                                log.trace("no order request to add line items too")
                                Thread.sleep(50)
                            }
                        }
                    }


                    // ====================================================== UPDATE

                    matches("u:order-request", loop) -> threads += Thread {
                        this.runMeasured("u_order_request") {
                            val orderRequestId = this.entityIdService.getOrderRequestFromUpdateWorkQueue()
                            if (orderRequestId.isPresent) {
                                this.orderRequestCmd.patchOrderRequest(orderRequestId.get())
                            }
                            else {
                                log.trace("no order request to update")
                                Thread.sleep(50)
                            }
                        }
                    }


                    // ======================================================== READ

                    matches("r:customer", loop) -> threads += Thread {
                        this.runMeasured("r_customer") {
                            this.customerCmd.getLastCustomer(false)
                        }
                    }

                    matches("r:addresses", loop) -> threads += Thread {
                        this.runMeasured("r_addresses") {
                            this.addressCmd.getLastCustomerAddresses(false)
                        }
                    }

                    matches("r:seller", loop) -> threads += Thread {
                        this.runMeasured("r_seller") {
                            this.sellerCmd.getLastSeller(false)
                        }
                    }

                    matches("r:product", loop) -> threads += Thread {
                        this.runMeasured("r_product") {
                            this.productCmd.getLastProduct(false)
                        }
                    }

                    matches("r:order-request", loop) -> threads += Thread {
                        this.runMeasured("r_order_request") {
                            this.orderRequestCmd.getLastOrderRequest(false)
                        }
                    }

                    matches("r:line-items", loop) -> threads += Thread {
                        this.runMeasured("r_line_item") {
                            this.lineItemCmd.getLastOrderRequestLineItems(false)
                        }
                    }


                    // ======================================================= ERROR

                    else -> throw CliException("unknown loop command ignored: $loop")
                }
            }

        }

        for (thread in threads)
            thread.start()

        for (thread in threads)
            thread.join()
    }

    private fun runMeasured(
        name: String,
        runnable: () -> Unit,
    ) {

        // TODO remove custom timing, use micrometer.
        var errCount = 0
        var okCount = 0
        val avg = mutableListOf<Long>()
        var lastReport = System.currentTimeMillis()

        val makerGaOk = AtomicInteger(0)
        val makerGaErr = AtomicInteger(0)
        val makerGaTimer = AtomicReference(0.0)

        // FIXME metric names to conform with Prometheus conventions.
        this.meterRegistry.gauge("maker__ga_ok__$name", listOf(Tag.of(TAG, TAG_VALUE)), makerGaOk) {
            it.get().toDouble()
        }
        this.meterRegistry.gauge("maker__ga_err__$name", listOf(Tag.of(TAG, TAG_VALUE)), makerGaErr) {
            it.get().toDouble()
        }
        this.meterRegistry.gauge("maker__ga_timer__$name", listOf(Tag.of(TAG, TAG_VALUE)), makerGaTimer) {
            it.get()
        }

        while (true) {

            val then = System.currentTimeMillis()

            try {
                runnable()
                okCount++
            }
            catch (e: Exception) {
                log.trace("{} error: {} -> {}", name, e::class.java, e.message)
                errCount++
            }

            val now = System.currentTimeMillis()

            avg += now - then

            val nextReport = lastReport + 1000
            if (nextReport < now) {
                lastReport = now
                val sum = avg.sum()

                log.info(
                    "{} time/req={} tx/sec={}",
                    name,
                    sum / avg.size,
                    okCount + errCount
                )

                makerGaOk.set(okCount)
                makerGaErr.set(errCount)
                makerGaTimer.set(sum.toDouble() / avg.size)
                errCount = 0
                okCount = 0

                avg.clear()
            }
        }
    }

    private fun extractCount(command: String): Pair<String, Int> {
        val split = command.split(":")
        return when (split.size) {
            1 -> command to 1
            2 -> command to 1
            else -> (split[0] + ":" + split[1]) to split[2].toInt()
        }
    }

}
