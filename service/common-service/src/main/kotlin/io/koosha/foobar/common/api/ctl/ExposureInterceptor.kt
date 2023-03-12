package io.koosha.foobar.common.api.ctl

import io.koosha.foobar.common.PROFILE__EXPOSE
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.servlet.AsyncHandlerInterceptor


@Profile(PROFILE__EXPOSE)
@Component
// ( private val tracer: Tracer, )
class ExposureInterceptor : AsyncHandlerInterceptor {

    @Suppress("UastIncorrectHttpHeaderInspection")
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {

        // val traceId = this.tracer.currentSpan()?.context()?.traceId()
        // if (traceId != null)
        //     response.addHeader("X-Foobar-B3-TraceId", traceId)
        //
        // val spanId = this.tracer.currentSpan()?.context()?.spanId()
        // if (spanId != null)
        //     response.addHeader("X-Foobar-B3-SpanId", spanId)
        //
        // val parentSpanId = this.tracer.currentSpan()?.context()?.parentId()
        // if (parentSpanId != null)
        //     response.addHeader("X-Foobar-B3-ParentSpanId", parentSpanId)

        return true
    }

}
