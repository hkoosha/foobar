package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.loader.api.connect.OrderRequestApi
import io.koosha.foobar.loader.api.service.queue.WorkQueue
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.Future


@Service
class LineItemUpdaterService(
    @Value("\${foobar.line-item-updater.num-tasks:128}")
    private val numTasks: Int,

    private val orderRequestApi: OrderRequestApi,
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
        log.error("error", e)
        false
    }

    fun generate(): Boolean {

        val orderRequestId: UUID = this.queue.getOrderRequestForState()

        val req = OrderRequestApi.UpdateDto(
            state = OrderRequestApi.OrderRequestState.LIVE
        )

        try {
            this.orderRequestApi.update(orderRequestId, req)
            return true
        }
        catch (e: Exception) {
            log.error("error", e)
            return false
        }
    }

}
