package io.koosha.foobar.seller.api.cfg

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MeterConfig {

    @Bean
    fun foobarTimedAspect(meterRegistry: MeterRegistry) = TimedAspect(meterRegistry)

}
