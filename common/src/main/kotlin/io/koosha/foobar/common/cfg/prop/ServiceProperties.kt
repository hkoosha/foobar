package io.koosha.foobar.common.cfg.prop

import io.koosha.foobar.common.conv.PortlessUrl
import org.springframework.validation.annotation.Validated
import java.net.URI
import javax.validation.ConstraintViolation
import javax.validation.Validator
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


@Validated
data class ServiceProperties(
    @field:PortlessUrl
    val url: String,

    @field:Min(PORT_MIN)
    @field:Max(PORT_MAX)
    @field:NotNull
    val port: Int,

    @field:NotNull
    val retry: Boolean,

    @field:NotNull
    val limit: Boolean,
) {

    companion object {
        private const val PORT_MAX = 65535L
        private const val PORT_MIN = 1L

        fun validate(
            validator: Validator,
            services: Map<String, ServiceProperties>,
            vararg requiredServices: String,
        ) {

            val nameToError = mutableMapOf<String, Set<ConstraintViolation<ServiceProperties>>>()

            services.forEach { (name, properties) ->
                val errors = validator.validate(properties)
                if (errors.isNotEmpty())
                    nameToError[name] = errors
            }

            if (nameToError.isNotEmpty())
                throw IllegalArgumentException(nameToError.toString())

            val missingServices = requiredServices
                .toList()
                .filterNot { services.containsKey(it) }
                .joinToString()

            if (missingServices.isNotEmpty())
                throw java.lang.IllegalArgumentException(
                    "following services are missing in application configuration: $missingServices"
                )
        }
    }


    fun address(): String {

        val uri = URI(this.url)

        val reconstruct = URI(
            uri.scheme.lowercase(),
            uri.userInfo,
            uri.host,
            this.port,
            uri.path,
            uri.query,
            uri.fragment
        )

        return reconstruct.toString()
    }

}
