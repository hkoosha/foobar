package io.koosha.foobar.common.cfg

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FoobarPrometheusConfig {

    @Bean
    fun foobarTimedAspect(meterRegistry: MeterRegistry) = TimedAspect(meterRegistry)

}
