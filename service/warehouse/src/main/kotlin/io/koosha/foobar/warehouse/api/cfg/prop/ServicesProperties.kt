package io.koosha.foobar.warehouse.api.cfg.prop

import io.koosha.foobar.common.cfg.prop.ServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct
import javax.validation.Valid


@ConfigurationProperties("foobar.service")
@ConstructorBinding
@Validated
data class ServicesProperties(
    @field:Valid
    private val services: Map<String, ServiceProperties>,
) {

    @PostConstruct
    fun validate() {
        val missing = listOf("seller")
            .filter { !this.services.containsKey(it) }
        if (missing.isNotEmpty())
            throw IllegalArgumentException(
                "following services are missing in application configuration: " +
                        missing.joinToString()
            )
    }


    fun seller(): ServiceProperties = this.services["seller"]!!

}