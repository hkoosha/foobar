package io.koosha.foobar.warehouse.api.cfg.prop

import io.koosha.foobar.common.cfg.prop.ServiceProperties
import jakarta.validation.Valid
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct


@ConfigurationProperties("foobar.service")
@Validated
data class ServicesProperties @ConstructorBinding constructor(
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
