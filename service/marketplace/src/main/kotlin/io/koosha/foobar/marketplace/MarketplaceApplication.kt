package io.koosha.foobar.marketplace

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class MarketplaceApplication

fun main(vararg args: String) {
    runApplication<MarketplaceApplication>(*args)
}
