package io.koosha.foobar.evil

import io.koosha.foobar.evil.api.svc.EvilService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["io.koosha.foobar"])
@ConfigurationPropertiesScan
class EvilApplication


fun main(vararg args: String) {

    val ctx = runApplication<EvilApplication>(*args)

    val evil = ctx.getBean(EvilService::class.java)

    evil.run()

    while (evil.running)
        try {
            Thread.sleep(10_000)
        }
        catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
            evil.running = false
            return
        }
}
