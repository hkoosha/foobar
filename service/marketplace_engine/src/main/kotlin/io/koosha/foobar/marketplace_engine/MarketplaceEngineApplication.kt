package io.koosha.foobar.marketplace_engine

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class MarketplaceEngineApplication


fun main(args: Array<String>) {
    runApplication<MarketplaceEngineApplication>(*args)
}
