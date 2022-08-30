package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.connect.marketplace.generated.api.LineItemRequest
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItemApi
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.queue.WorkQueue
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future


@Service
class LineItemGeneratorService(
    @Value("\${foobar.line-item-generator.num-tasks:128}")
    private val numTasks: Int,

    @Value("\${foobar.line-item-generator.max-line-items:20}")
    private val numLineItems: Int,

    private val rand: Rand,

    private val lineItemApi: OrderRequestLineItemApi,

    private val ids: IdStorageService,
    private val queue: WorkQueue,
) {

    private val log = KotlinLogging.logger {}

    private val executorService = Executors.newFixedThreadPool(this.numTasks)


    fun generate(running: () -> Boolean) {

        log.info("generating...")

        while (running()) {

            log.info("starting generation batch")

            val finish = mutableListOf<Future<*>>()
            for (i in 0 until this.numTasks)
                finish += this.executorService.submit {
                    this.tryGenerate()
                }

            for (f in finish)
                f.get()

            log.info("generation batch finished")
        }

        this.executorService.shutdown()
    }

    fun tryGenerate(): Boolean = try {
        this.generate()
    }
    catch (e: Exception) {
        log.error("error:  ${e.javaClass.name} -> ${e.message}")
        false
    }

    fun generate(): Boolean {

        if (this.numLineItems >= this.ids.getProductRange().last)
            return false

        val orderRequestId = this.queue.getOrderRequestForLineItem()
        val productIds = this.ids.getRandomProducts(this.rand.int(max = this.numLineItems, min = 1))

        var ok = true

        for (productId in productIds) {

            val req = LineItemRequest()
            req.productId = productId
            req.units = this.rand.long(max = 100, min = 1)

            val response = this.lineItemApi.postLineItemWithHttpInfo(orderRequestId, req)
            if (response.statusCode !in 200..299)
                ok = false
        }

        this.queue.enqueueOrderRequestForState(orderRequestId)

        return ok
    }

}
