package io.koosha.foobar.warehouse

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class WarehouseServiceApplication


fun main(args: Array<String>) {
    runApplication<WarehouseServiceApplication>(*args)
}
