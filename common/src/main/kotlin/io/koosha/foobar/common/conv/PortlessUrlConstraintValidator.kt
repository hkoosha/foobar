package io.koosha.foobar.common.conv

import java.net.MalformedURLException
import java.net.URL
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class PortlessUrlConstraintValidator : ConstraintValidator<PortlessUrl, String> {

    private lateinit var annon: PortlessUrl

    override fun initialize(constraintAnnotation: PortlessUrl) {

        this.annon = constraintAnnotation
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {

        if (value == null)
            return this.annon.allowNull

        if (value.isEmpty())
            return this.annon.allowEmpty

        return try {
            val url = URL(value)
            url.port == -1
        }
        catch (e: MalformedURLException) {
            false
        }

    }

}
