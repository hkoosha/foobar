package io.koosha.foobar.marketplace.api.cfg

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.client.WebClient


@Configuration(proxyBeanMethods = false)
@EnableWebFlux
class WebFluxConfig {

    @Bean
    fun webClient(): WebClient =
        WebClient
            .builder()
            .build()

}
