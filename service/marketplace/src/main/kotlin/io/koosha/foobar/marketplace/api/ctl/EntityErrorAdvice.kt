package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.ApiError
import io.koosha.foobar.common.model.EntityBadValueApiError
import io.koosha.foobar.common.model.EntityIllegalStateApiError
import io.koosha.foobar.common.model.EntityNotFoundApiError
import io.koosha.foobar.common.model.ServerError
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors


@ConditionalOnWebApplication
@RestControllerAdvice
class EntityErrorAdvice {

    companion object {

        const val INTERNAL_ERROR = "internal server error"
        const val ENTITY_NOT_FOUND_MSG = "entity or entities not found"
        const val ENTITY_BAD_VALUE_MSG = "bad value for entity"
        const val ENTITY_ILLEGAL_STATE_MSG = "operation not allowed with current state of entity"

    }

    private val log = KotlinLogging.logger {}

    // @ExceptionHandler(EntityNotFoundException::class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    // @ResponseBody
    fun entityNotFoundError(ex: EntityNotFoundException): EntityNotFoundApiError = EntityNotFoundApiError(
        message = ENTITY_NOT_FOUND_MSG,
        context = ex.context,
    )

    // @ExceptionHandler(EntityBadValueException::class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ResponseBody
    fun entityBadValueError(ex: EntityBadValueException) = EntityBadValueApiError(
        message = ENTITY_BAD_VALUE_MSG,
        context = ex.errors
            ?.stream()
            ?.collect(
                Collectors.groupingBy(
                    { it.propertyPath.toString() },
                    Collectors.mapping({ it.message }, Collectors.toList())
                )
            )
            ?: emptyMap(),
    )

    // @ExceptionHandler(EntityInIllegalStateException::class)
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    // @ResponseBody
    fun entityIllegalStateError(ex: EntityInIllegalStateException) = EntityIllegalStateApiError(
        message = ENTITY_ILLEGAL_STATE_MSG,
        context = ex.context,
    )

    @ExceptionHandler(
        Throwable::class
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun otherError(ex: Throwable): ApiError =
        when (ex) {
            is EntityNotFoundException -> this.entityNotFoundError(ex)
            is EntityBadValueException -> this.entityBadValueError(ex)
            is EntityInIllegalStateException -> this.entityIllegalStateError(ex)
            else -> {
                log.error("unexpected error", ex)
                ServerError(message = INTERNAL_ERROR, context = ex)
            }
        }

}
