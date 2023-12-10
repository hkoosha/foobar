package io.koosha.foobar.shipping

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class ShippingServiceApplication

fun main(vararg args: String) {
    runApplication<ShippingServiceApplication>(*args)
}
