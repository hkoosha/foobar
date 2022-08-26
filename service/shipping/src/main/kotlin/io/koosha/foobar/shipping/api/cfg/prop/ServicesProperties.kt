package io.koosha.foobar.shipping.api.cfg.prop

import io.koosha.foobar.common.cfg.prop.ServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import javax.annotation.PostConstruct
import javax.validation.Validator


@ConfigurationProperties("foobar.service")
@ConstructorBinding
data class ServicesProperties(
    private val services: Map<String, ServiceProperties>,
) : ApplicationContextAware {

    private lateinit var validator: Validator

    override fun setApplicationContext(ctx: ApplicationContext) {
        this.validator = ctx.getBean(Validator::class.java)
    }

    @PostConstruct
    fun validate() = ServiceProperties.validate(this.validator, this.services, "seller", "customer", "warehouse")


    fun seller(): ServiceProperties = this.services["seller"]!!

    fun customer(): ServiceProperties = this.services["customer"]!!

    fun warehouse(): ServiceProperties = this.services["warehouse"]!!

}
