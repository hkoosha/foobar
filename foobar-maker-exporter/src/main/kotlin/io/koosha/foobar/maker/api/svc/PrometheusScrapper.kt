package io.koosha.foobar.maker.api.svc

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class PrometheusScrapper {

    companion object {
        private const val PROMETHEUS_TAG =
            """{foobar_service="foobar-maker-exporter",foobar_tool="foobar-maker-exporter",} """
    }

    private val log = KotlinLogging.logger {}

    private final val lock = Any()

    // TODO volatile needed?
    @Volatile
    private var lastReadContent = ""

    @Volatile
    private var lastSuccessfulRead = 0L

    @Volatile
    private var lastError: String? = "not scrapped yet"

    @Volatile
    private var numScraps = 0

    private val client = OkHttpClient()

    fun getLastRead(): String = synchronized(this.lock) {

        this.update()
        this.header() + this.lastReadContent
    }

    private fun update() {

        this.numScraps++

        this.lastReadContent =
            try {
                val read = client
                    .newCall(Request.Builder().url("http://foobar-maker:8080/actuator/prometheus").build())
                    .execute()
                    .use {
                        it.body?.string() ?: throw RuntimeException("received empty body")
                    }
                this.lastSuccessfulRead = System.currentTimeMillis()
                this.lastError = null
                read
            }
            catch (e: Exception) {
                log.trace("failed to scarp", e)
                log.error("failed to scarp, ${e.javaClass} -> ${e.message}")
                log.warn("zeroing out")
                this.lastError = (e.javaClass.name + " -> " + e.message).replace("\n", " ")
                this.zeroOut(this.lastReadContent)
            }
    }

    private fun zeroOut(content: String): String {

        val report = StringBuilder()

        content
            .lines()
            .stream()
            .map {
                if (it.startsWith("maker__")) {
                    val lastSpace = it.lastIndexOf(' ')
                    val start = it.substring(0 until lastSpace)
                    "$start 0.0"
                }
                else {
                    it
                }
            }
            .forEach {
                report.append(it)
                    .append("\n")
            }

        return report.toString()
    }

    private fun header(): String {

        val header = StringBuilder()

        header
            .append("# last error: ")
            .append(this.lastError)
            .append("\n")

        header
            .append("# last successful read: ")
            .append(Instant.ofEpochMilli(this.lastSuccessfulRead))
            .append("\n")

        header
            .append("maker_exporter__last_successful_read")
            .append(PROMETHEUS_TAG)
            .append(this.lastSuccessfulRead)
            .append("\n")

        header
            .append("maker_exporter__num_scraps")
            .append(PROMETHEUS_TAG)
            .append(this.numScraps)
            .append("\n")

        return header.toString()
    }

}
