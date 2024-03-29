package io.koosha.foobar.marketplace.api.cfg.prop

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
        val missing = listOf("customer", "seller", "warehouse")
            .filter { !this.services.containsKey(it) }
        if (missing.isNotEmpty())
            throw IllegalArgumentException(
                "following services are missing in application configuration: " +
                        missing.joinToString()
            )
    }


    fun seller(): ServiceProperties = this.services["seller"]!!

    fun customer(): ServiceProperties = this.services["customer"]!!

    fun warehouse(): ServiceProperties = this.services["warehouse"]!!

}
