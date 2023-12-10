package io.koosha.foobar.seller

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class SellerServiceApplication

fun main(vararg args: String) {
    runApplication<SellerServiceApplication>(*args)
}
