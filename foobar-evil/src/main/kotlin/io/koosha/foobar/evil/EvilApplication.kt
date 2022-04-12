package io.koosha.foobar.evil

import io.koosha.foobar.evil.api.svc.EvilService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.io.InterruptedIOException


@SpringBootApplication(scanBasePackages = ["io.koosha.foobar"])
@ConfigurationPropertiesScan
class EvilApplication


fun main(args: Array<String>) {

    val ctx = runApplication<EvilApplication>(*args)

    val evil = ctx.getBean(EvilService::class.java)

    evil.run()

    while (evil.running)
        try {
            Thread.sleep(10_000)
        }
        catch (ex: InterruptedIOException) {
            Thread.currentThread().interrupt()
            evil.running = false
            return
        }
}
