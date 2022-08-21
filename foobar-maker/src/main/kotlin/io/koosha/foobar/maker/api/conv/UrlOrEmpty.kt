package io.koosha.foobar.maker.api.conv

import java.net.URL
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UrlOrEmptyConstraintValidator::class])
annotation class UrlOrEmpty(

    val message: String = "invalid URL",

    val groups: Array<KClass<Any>> = [],

    val payload: Array<KClass<out Payload>> = [],
)


class UrlOrEmptyConstraintValidator : ConstraintValidator<UrlOrEmpty, String> {

    @Suppress("SwallowedException")
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {

        if (value == null || value.trim().isEmpty())
            return true

        return try {
            URL(value)
            true
        }
        catch (@Suppress("SwallowedException") e: Exception) {
            false
        }
    }

}
