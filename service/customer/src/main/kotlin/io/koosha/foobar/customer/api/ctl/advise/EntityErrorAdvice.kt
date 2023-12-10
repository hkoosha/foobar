package io.koosha.foobar.customer.api.ctl.advise

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.ApiError
import io.koosha.foobar.common.model.EntityBadValueApiError
import io.koosha.foobar.common.model.EntityIllegalStateApiError
import io.koosha.foobar.common.model.EntityNotFoundApiError
import io.koosha.foobar.common.model.ServerError
import io.koosha.foobar.customer.api.error.ResourceCurrentlyUnavailableException
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors


@ConditionalOnWebApplication
@RestControllerAdvice
class EntityErrorAdvice {

    // @Value("\${foobar.rest.on-concurrency-error-retry-after}")
    // onCurrencyErrorRetryAfter: Duration,
    // private val onCurrencyErrorRetryAfter = onCurrencyErrorRetryAfter.toMillis().toString()

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun entityNotFoundError(ex: EntityNotFoundException): EntityNotFoundApiError = EntityNotFoundApiError(
        message = "entity or entities not found",
        context = ex.context,
    )

    @ExceptionHandler(EntityBadValueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun entityBadValueError(ex: EntityBadValueException) = EntityBadValueApiError(
        message = "bad value for entity",
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

    @ExceptionHandler(EntityInIllegalStateException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun entityIllegalStateError(ex: EntityInIllegalStateException) = EntityIllegalStateApiError(
        message = "operation not allowed with current state of entity",
        context = ex.context,
    )

    @ExceptionHandler(
        ConcurrencyFailureException::class,
        ResourceCurrentlyUnavailableException::class,
    )
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    fun onRetryableError(ex: Exception): ApiError =
        ServerError(message = "resource is currently unavailable", context = ex)

}
