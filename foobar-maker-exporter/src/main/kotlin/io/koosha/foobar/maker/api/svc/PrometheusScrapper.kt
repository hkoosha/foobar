package io.koosha.foobar.maker.api.svc

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class PrometheusScrapper {

    private val log = KotlinLogging.logger {}

    private final val lock = Any()

    @Volatile
    private var lastReadContent = ""

    @Volatile
    private var lastRead = 0L

    @Volatile
    private var lastError: String? = "not scrapped yet"

    private val client = OkHttpClient()

    fun getLastRead(): String {

        synchronized(this.lock) {

            if ((this.lastRead + Duration.ofSeconds(5).toMillis()) < System.currentTimeMillis())
                this.update()

            return if (this.lastError == null)
                this.lastReadContent
            else
                "# " + this.lastError + "\n" + this.lastReadContent
        }
    }

    private fun update() {

        val request = Request.Builder()
            .url("http://foobar-maker:8080/actuator/prometheus")
            .build()

        try {
            this.lastReadContent = client.newCall(request).execute().use {
                it.body?.string() ?: throw RuntimeException("received empty body")
            }
            this.lastRead = System.currentTimeMillis()
            this.lastError = null
        }
        catch (e: Exception) {
            log.error("failed to scarp", e)
            this.lastError = (e.javaClass.name + " -> " + e.message).replace("\n", " ")
        }
    }

}
