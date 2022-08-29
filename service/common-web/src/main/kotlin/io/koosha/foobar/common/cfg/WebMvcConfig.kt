package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__DEBUG_WEB
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.AsyncHandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Profile(PROFILE__DEBUG_WEB)
@ConditionalOnWebApplication
@Configuration
class WebMvcConfig : WebMvcConfigurer {

    companion object {

        private const val SEPARATOR = "  -  "

    }

    private val interceptor = object : AsyncHandlerInterceptor {

        val log = KotlinLogging.logger {}

        override fun afterCompletion(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            ex: Exception?,
        ) {

            val message = StringBuilder()
                .append("method: ").append(request.method).append(SEPARATOR)
                .append("uri: ").append(request.requestURI).append(SEPARATOR)
                .append("status: ").append(response.status).append(SEPARATOR)
                .toString()

            if (ex != null)
                log.error(ex) { message }
            else
                log.info { message }
        }
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(this.interceptor)
    }

}
