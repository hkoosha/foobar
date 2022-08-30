package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestUpdateRequest
import io.koosha.foobar.loader.api.service.queue.WorkQueue
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future


@Service
class LineItemUpdaterService(
    @Value("\${foobar.line-item-updater.num-tasks:128}")
    private val numTasks: Int,

    private val orderRequestApi: OrderRequestApi,

    private val queue: WorkQueue,
) {

    private val log = KotlinLogging.logger {}

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
                log.info("generation batch finished")
        }

        this.executorService.shutdown()
    }

    fun tryGenerate(): Boolean = try {
        if (!this.generate()) {
            log.error("line_item updater failed")
            false
        }
        else {
            true
        }
    }
    catch (e: Exception) {
        log.error("error:  ${e.javaClass.name} -> ${e.message}")
        false
    }

    fun generate(): Boolean {

        val orderRequestId = this.queue.getOrderRequestForState()

        val req = OrderRequestUpdateRequest()
        req.state = OrderRequestUpdateRequest.StateEnum.LIVE

        val response = this.orderRequestApi.patchOrderRequestWithHttpInfo(orderRequestId, req)
        return response.statusCode in 200..299
    }

}
