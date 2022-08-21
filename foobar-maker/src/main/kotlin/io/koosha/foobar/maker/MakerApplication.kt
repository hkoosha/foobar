package io.koosha.foobar.maker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["io.koosha.foobar"])
@ConfigurationPropertiesScan
class MakerApplication


fun main(vararg args: String) {
    runApplication<MakerApplication>(*args)
}
