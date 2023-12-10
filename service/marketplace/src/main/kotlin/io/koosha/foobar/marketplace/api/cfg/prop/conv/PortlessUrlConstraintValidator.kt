package io.koosha.foobar.marketplace.api.cfg.prop.conv

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.net.MalformedURLException
import java.net.URI


class PortlessUrlConstraintValidator : ConstraintValidator<PortlessUrl, String> {

    private lateinit var annon: PortlessUrl

    override fun initialize(constraintAnnotation: PortlessUrl) {

        this.annon = constraintAnnotation
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean =
        when {
            value == null -> this.annon.allowNull
            value.isEmpty() -> this.annon.allowEmpty
            else -> try {
                val url = URI.create(value).toURL()
                url.port == -1
            }
            catch (e: MalformedURLException) {
                false
            }
        }

}
