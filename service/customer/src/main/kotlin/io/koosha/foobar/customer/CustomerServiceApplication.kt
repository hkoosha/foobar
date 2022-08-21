package io.koosha.foobar.customer

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class CustomerServiceApplication


fun main(vararg args: String) {
    runApplication<CustomerServiceApplication>(*args)
}
