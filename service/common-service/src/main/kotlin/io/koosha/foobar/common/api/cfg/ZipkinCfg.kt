package io.koosha.foobar.common.api.cfg

import brave.sampler.RateLimitingSampler
import brave.sampler.Sampler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ZipkinCfg {

    @Bean
    fun defaultSampler(
        @Value("\${foobar.zipkin.sampler.rate:ALWAYS}")
        rate: String,
    ): Sampler = when {
        rate.uppercase() == "ALWAYS" -> Sampler.ALWAYS_SAMPLE

        rate.uppercase() == "NEVER" -> Sampler.NEVER_SAMPLE

        rate.uppercase().startsWith("PROBABILITY:") -> Sampler.create(
            rate.substring("PROBABILITY:".length).toFloat()
        )

        rate.uppercase().startsWith("rate:") -> RateLimitingSampler.create(
            rate.substring("rate:".length).toInt()
        )

        else -> throw IllegalArgumentException("can not parse rate: $rate")
    }

}