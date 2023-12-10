package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.loader.api.connect.OrderRequestLineItemApi
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.queue.WorkQueue
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID
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

    private val log = LoggerFactory.getLogger(this::class.java)

    private val executorService = Executors.newFixedThreadPool(this.numTasks)

    fun generate(running: () -> Boolean) {

        log.info("generating...")

        var i = 0

        while (running()) {

            i++
            if (i == Int.MAX_VALUE)
                i = 0
            if (i > 0 && i % 10000 == 0)
                log.info("generation batch started")

            val finish = mutableListOf<Future<*>>()
            for (j in 0 until this.numTasks)
                finish += this.executorService.submit {
                    this.tryGenerate()
                }

            for (f in finish)
                f.get()

            if (i > 0 && i % 10000 == 0)
                log.info("generation batch completed")
        }

        this.executorService.shutdown()
    }

    fun tryGenerate(): Boolean = try {
        if (!this.generate()) {
            log.error("line_item generator failed")
            false
        }
        else {
            true
        }
    }
    catch (e: Exception) {
        log.error("error", e)
        false
    }

    fun generate(): Boolean {

        if (this.numLineItems >= this.ids.getProductRange().last) {
            log.error("err 0")
            return false
        }

        val orderRequestId: UUID = this.queue.getOrderRequestForLineItem()
        val productIds: Set<UUID> = this.ids.getRandomProducts(this.rand.int(max = this.numLineItems, min = 1))

        var ok = true

        for (productId in productIds) {

            val req = OrderRequestLineItemApi.CreateDto(
                productId = productId,
                units = this.rand.long(max = 50, min = 1)
            )

            try {
                this.lineItemApi.create(orderRequestId, req)
            }
            catch (e: Exception) {
                log.error("error", e)
                ok = false
            }
        }

        this.queue.enqueueOrderRequestForState(orderRequestId)

        return ok
    }

}
