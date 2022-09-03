package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.*


@Component
@Suppress("MagicNumber")
class LoopCmd(
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
        private const val LOG_EVERY = 200

        // TODO remove this, no seller will have all products. Only 1 works.
        private const val PROD_COUNT = 1
        private const val UNITS = 3L
    }

    private val log = KotlinLogging.logger {}

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
            for (i in 0 until count)
                when {
                    // ====================================================== CREATE

                    matches("c:customer", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.customerCmd.postCustomer(args, doLog = false)
                        }
                    }

                    matches("c:address", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            // TODO limit number of addresses allowed in customer-api.
                            this.addressCmd.postAddress(args, emptyList(), doLog = false)
                        }
                    }

                    matches("c:seller", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.sellerCmd.postSeller(args, doLog = false)
                        }
                    }

                    matches("c:product", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.productCmd.postProduct(args, doLog = false)
                        }
                    }

                    matches("c:availability", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.availabilityCmd.postAvailability(args, emptyList(), doLog = false)
                        }
                    }

                    matches("c:order-request", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.orderRequestCmd.postOrderRequest(emptyList(), doLog = false)
                        }
                    }

                    matches("c:line-item", loop) -> threads += Thread {

                        // TODO limit number of line items allowed in warehouse-api.
                        this.runMeasured(loop) {
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
                        this.runMeasured(loop) {
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
                        this.runMeasured(loop) {
                            this.customerCmd.getLastCustomer(false)
                        }
                    }

                    matches("r:addresses", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.addressCmd.getLastCustomerAddresses(false)
                        }
                    }

                    matches("r:seller", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.sellerCmd.getLastSeller(false)
                        }
                    }

                    matches("r:product", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.productCmd.getLastProduct(false)
                        }
                    }

                    matches("r:order-request", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.orderRequestCmd.getLastOrderRequest(false)
                        }
                    }

                    matches("r:line-items", loop) -> threads += Thread {
                        this.runMeasured(loop) {
                            this.lineItemCmd.getLastOrderRequestLineItems(false)
                        }
                    }


                    // ======================================================= ERROR

                    else -> throw CliException("unknown loop command ignored: $loop")
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

        var count = 0
        var i = 0
        val avg = LongArray(LOG_EVERY)
        var lastReport = System.currentTimeMillis()
        var tx = 0
        while (true) {

            val then = System.currentTimeMillis()

            try {
                runnable()
            }
            catch (e: Exception) {
                log.trace("$name error: ${e.javaClass.name} -> ${e.message}")
            }

            val now = System.currentTimeMillis()

            avg[i] = now - then

            i++
            count++
            tx++

            // TODO fix we're skipping one measurement
            if (count > 0 && count % LOG_EVERY == 0) {
                i = 0
                log.info(
                    "{} total={}, time/req={}",
                    name,
                    count,
                    avg.sum() / LOG_EVERY,
                )
            }

            val nextReport = lastReport + 1000
            if (nextReport < now) {
                log.info("{} tx/second: {}", name, tx)
                lastReport = now
                tx = 0
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
