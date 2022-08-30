package io.koosha.foobar.evil.api.cfg

import okhttp3.OkHttpClient
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class EvilConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun okHttp(): OkHttpClient = OkHttpClient()

}
