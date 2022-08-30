package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestCreateRequest
import io.koosha.foobar.loader.api.service.queue.WorkQueue
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor


@Service
class OrderRequestGeneratorService(
    @Value("\${foobar.order-request-generator.num-tasks:128}")
    private val numTasks: Int,

    private val orderRequestApi: OrderRequestApi,

    private val ids: IdStorageService,
    private val queue: WorkQueue,
) {

    private val log = KotlinLogging.logger {}

    private val executorService = Executors.newFixedThreadPool(this.numTasks) as ThreadPoolExecutor


    fun generate(running: () -> Boolean) {

        log.info("generating...")

        var i = 0

        while (running()) {

            i++
            if (i == Int.MAX_VALUE)
                i = 0
            if (i > 0 && i % 10_000 == 0)
                log.info("generation batch started")

            val finish = mutableListOf<Future<*>>()
            for (j in ids.getCustomerRange().step(this.numTasks))
                finish += this.executorService.submit {
                    this.generate(j until (j + this.numTasks))
                }

            for (f in finish)
                f.get()

            if(finish.size == 0) {
                log.warn("no customer available, sleeping: ${ids.getCustomerRange()}")
                Thread.sleep(100)
            }

            if (i > 0 && i % 10_000 == 0)
                log.info("generation batch completed, running: ${executorService.activeCount}")
        }

        this.executorService.shutdown()
    }

    fun generate(customerRange: IntRange) {

        for (customer in customerRange)
            this.tryGenerate(customer)
    }


    fun tryGenerate(customerIndex: Int): Boolean = try {
        if (!this.generate(customerIndex)) {
            log.error("customer generator failed for customer index: $customerIndex")
            false
        }
        else {
            true
        }
    }
    catch (e: Exception) {
        log.error("error: customerIndex=$customerIndex, ${e.javaClass.name} -> ${e.message}")
        false
    }

    fun generate(customerIndex: Int): Boolean {

        val customerId = this.ids.getCustomerId(customerIndex) ?: return false

        val req = OrderRequestCreateRequest()
        req.customerId = customerId

        val response = this.orderRequestApi.postOrderRequestWithHttpInfo(req)
        val ok = response.statusCode in 200..299

        if (ok)
            this.queue.enqueueOrderRequestForLineItem(response.data.orderRequestId)

        return ok
    }

}
