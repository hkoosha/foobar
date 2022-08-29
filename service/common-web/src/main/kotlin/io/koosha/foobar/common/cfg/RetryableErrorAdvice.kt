package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Duration
import javax.servlet.http.HttpServletResponse


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
    fun onRetryableError(response: HttpServletResponse) =
        response.setHeader(HttpHeaders.RETRY_AFTER, this.onCurrencyErrorRetryAfter)

}
