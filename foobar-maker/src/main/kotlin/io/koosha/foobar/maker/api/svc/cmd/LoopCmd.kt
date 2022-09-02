package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.matches
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component


@Component
class LoopCmd(
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

        for (loop in freeArgs) {
            when {
                // ====================================================== CREATE

                matches("c:customer", loop) -> threads += Thread {
                    this.runMeasured(loop) {
                        this.customerCmd.postCustomer(args, doLog = false)
                    }
                }

                matches("c:address", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            // Make sure new customers are kept being added so address list does not explode.
                            // TODO limit number of addresses allowed in customer-api.
                            this.customerCmd.postCustomer(args, doLog = false)
                            this.addressCmd.postAddress(args, emptyList(), doLog = false)
                        }
                }

                matches("c:seller", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.sellerCmd.postSeller(args, doLog = false)
                        }
                }

                matches("c:product", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.productCmd.postProduct(args, doLog = false)
                        }
                }

                matches("c:availability", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.availabilityCmd.postAvailability(args, emptyList(), doLog = false)
                        }
                }

                matches("c:order-request", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.orderRequestCmd.postOrderRequest(emptyList(), doLog = false)
                        }
                }

                matches("c:line-item", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            // Make sure new products are kept being added so line items list does not explode.
                            // TODO limit number of line items allowed in warehouse-api.
                            this.orderRequestCmd.postOrderRequest(emptyList(), doLog = false)
                            this.lineItemCmd.postLineItem(args, emptyList(), doLog = false)
                        }
                }


                // ======================================================== READ

                matches("r:customer", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.customerCmd.getLastCustomer(false)
                        }
                }

                matches("r:addresses", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.addressCmd.getLastCustomerAddresses(false)
                        }
                }

                matches("r:seller", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.sellerCmd.getLastSeller(false)
                        }
                }

                matches("r:product", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.productCmd.getLastProduct(false)
                        }
                }

                matches("r:order-request", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.orderRequestCmd.getLastOrderRequest(false)
                        }
                }

                matches("r:line-items", loop) -> threads += Thread {
                    while (true)
                        this.runMeasured(loop) {
                            this.lineItemCmd.getLastOrderRequestLineItems(false)
                        }
                }


                // ======================================================= ERROR

                else -> log.warn("unknown loop command ignored: {}", loop)
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
                log.error("$name error: ${e.javaClass.name} -> ${e.message}")
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

}
