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


@Service
class OrderRequestGeneratorService(
    @Value("\${foobar.order-request-generator.num-tasks:128}")
    private val numTasks: Int,

    private val orderRequestApi: OrderRequestApi,

    private val ids: IdStorageService,
    private val queue: WorkQueue,
) {

    private val log = KotlinLogging.logger {}

    private val executorService = Executors.newFixedThreadPool(this.numTasks)


    fun generate(running: () -> Boolean) {

        while (running()) {
            val finish = mutableListOf<Future<*>>()
            for (i in ids.getCustomerRange().step(this.numTasks))
                finish += this.executorService.submit {
                    this.generate(i until (i + this.numTasks))
                }
            for (f in finish)
                f.get()
        }

        this.executorService.shutdown()
    }

    fun generate(customerRange: IntRange) {

        for (customer in customerRange)
            this.tryGenerate(customer)
    }


    fun tryGenerate(customerIndex: Int): Boolean = try {
        this.generate(customerIndex)
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
