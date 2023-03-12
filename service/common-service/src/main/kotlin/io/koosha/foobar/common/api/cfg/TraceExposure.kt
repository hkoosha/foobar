package io.koosha.foobar.common.api.cfg

import io.koosha.foobar.common.PROFILE__EXPOSE
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Profile(PROFILE__EXPOSE)
@Configuration
// (private val interceptor: ExposureInterceptor)
class TraceExposure : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        // registry.addInterceptor(this.interceptor)
    }

}
