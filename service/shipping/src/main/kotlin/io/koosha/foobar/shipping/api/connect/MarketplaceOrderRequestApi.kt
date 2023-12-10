package io.koosha.foobar.shipping.api.connect

import feign.FeignException
import io.koosha.foobar.shipping.api.model.dto.OrderRequestDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.CircuitBreaker
import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "MarketplaceOrderRequest",
    url = "\${foobar.service.services.marketplace.url}:\${foobar.service.services.marketplace.port}",
    path = "/foobar/marketplace/v1",
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
interface MarketplaceOrderRequestApi {

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/order-request/{orderRequestId}"]
    )
    fun getOrderRequest(
        @PathVariable("orderRequestId")
        orderRequestId: UUID,
    ): OrderRequestDto

}
