package io.koosha.foobar.marketplace.api.cfg.prop.conv

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PortlessUrlConstraintValidator::class])
annotation class PortlessUrl(

    val message: String = "Invalid portless URL, either url is malformed or an explicit port is specified",

    val groups: Array<KClass<Any>> = [],

    val payload: Array<KClass<out Payload>> = [],

    val allowEmpty: Boolean = false,

    val allowNull: Boolean = false,
)
