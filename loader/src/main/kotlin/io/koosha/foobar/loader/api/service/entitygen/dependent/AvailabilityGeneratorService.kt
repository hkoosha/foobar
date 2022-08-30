package io.koosha.foobar.loader.api.service.entitygen.dependent

import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityApi
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityCreateRequest
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.Future


@Service
class AvailabilityGeneratorService(
    @Value("\${foobar.availability-generator.num-tasks:16}")
    private val numTasks: Int,

    private val rand: Rand,

    private val availabilityApi: AvailabilityApi,

    private val ids: IdStorageService,
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
            for (j in ids.getSellerRange().step(this.numTasks))
                finish += this.executorService.submit {
                    this.generate(j until (j + this.numTasks))
                }

            for (f in finish)
                f.get()

            if (i > 0 && i % 10000 == 0)
                log.info("generation batch completed")
        }

        this.executorService.shutdown()
    }

    fun generate(sellerRange: IntRange) {

        for (seller in sellerRange)
            this.tryGenerate(seller)
    }


    fun tryGenerate(sellerIndex: Int): Boolean = try {
        if (!this.generate(sellerIndex)) {
            log.error("availability generator failed")
            false
        }
        else {
            true
        }
    }
    catch (e: Exception) {
        log.error("error: sellerIndex=$sellerIndex, ${e.javaClass.name} -> ${e.message}")
        false
    }

    fun generate(sellerIndex: Int): Boolean {

        val sellerId = this.ids.getSellerId(sellerIndex) ?: return false

        var ok = true

        for (productIndex in this.ids.getProductRange()) {
            if (!this.rand.bool())
                continue

            val productId = this.ids.getProductId(productIndex)

            val req = AvailabilityCreateRequest()
            req.sellerId = sellerId
            req.unitsAvailable = this.rand.long(max = 10000, min = 10)
            req.pricePerUnit = this.rand.long(max = 100_000L, min = 10_000)

            val response = this.availabilityApi.postAvailabilityWithHttpInfo(productId, req)

            // TODO ignore availability already exists errors.
            ok = ok && response.statusCode >= 200 && response.statusCode < 300
        }

        return ok
    }

}
