package io.koosha.foobar.common.api.cfg

import io.koosha.foobar.common.PROFILE__EXPOSE
import io.koosha.foobar.common.api.ctl.ExposureInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Profile(PROFILE__EXPOSE)
@Configuration
class TraceExposure(
    private val interceptor: ExposureInterceptor,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(this.interceptor)
    }

}
