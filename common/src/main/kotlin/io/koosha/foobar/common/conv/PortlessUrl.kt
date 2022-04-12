package io.koosha.foobar.common.conv

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PortlessUrlConstraintValidator::class])
annotation class PortlessUrl(

    val message: String = "invalid URL",

    val groups: Array<KClass<Any>> = [],

    val payload: Array<KClass<out Payload>> = [],

    val allowEmpty: Boolean = false,

    val allowNull: Boolean = false,
)
