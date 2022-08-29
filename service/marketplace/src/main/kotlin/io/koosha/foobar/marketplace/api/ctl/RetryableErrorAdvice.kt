package io.koosha.foobar.marketplace.api.ctl

import io.koosha.foobar.marketplace.api.error.ResourceCurrentlyUnavailableException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.server.ServerResponse
import java.time.Duration


@ConditionalOnWebApplication
@RestControllerAdvice
class RetryableErrorAdvice(
    @Value("\${foobar.rest.on-concurrency-error-retry-after}")
    onCurrencyErrorRetryAfter: Duration,
) {

    private val onCurrencyErrorRetryAfter = onCurrencyErrorRetryAfter.toMillis().toString()

    @ExceptionHandler(
        ConcurrencyFailureException::class,
        ResourceCurrentlyUnavailableException::class,
    )
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun onRetryableError(response: ServerResponse) =
        response.headers().set(HttpHeaders.RETRY_AFTER, this.onCurrencyErrorRetryAfter)

}
