package io.koosha.foobar.common.conv

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.net.MalformedURLException
import java.net.URL


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
                val url = URL(value)
                url.port == -1
            }
            catch (e: MalformedURLException) {
                false
            }
        }

}
