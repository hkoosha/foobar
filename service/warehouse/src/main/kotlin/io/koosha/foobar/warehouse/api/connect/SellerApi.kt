package io.koosha.foobar.warehouse.api.connect

import feign.FeignException
import io.koosha.foobar.warehouse.api.model.dto.SellerDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.CircuitBreaker
import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "Seller",
    url = "\${foobar.service.services.seller.url}:\${foobar.service.services.seller.port}",
    path = "/foobar/seller/v1",
)
@CircuitBreaker(
    retryFor = [
        FeignException.FeignServerException::class,
        FeignException.TooManyRequests::class,
        FeignException.NotFound::class,
    ],
    notRecoverable = [Throwable::class],
    maxAttemptsExpression = "\${foobar.circuit-breaker.max-attempts:3}",
    resetTimeoutExpression = "\${foobar.circuit-breaker.reset-timeout-millis:20000}",
    openTimeoutExpression = "\${foobar.circuit-breaker.open-timeout-millis:5000}"
)
@Retryable(
    retryFor = [
        FeignException.FeignServerException::class,
        FeignException.TooManyRequests::class,
        FeignException.NotFound::class,
    ],
    notRecoverable = [Throwable::class],
    maxAttemptsExpression = "\${foobar.retry.max-attempts:3}",
    backoff = Backoff(
        delayExpression = "\${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "\${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "\${foobar.retry.multiplier:0}",
        randomExpression = "\${foobar.retry.random:false}",
    ),
)
interface SellerApi {

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/seller/{sellerId}"]
    )
    fun getSeller(
        @PathVariable("sellerId")
        sellerId: UUID,
    ): SellerDto

}
