package io.koosha.foobar.maker.api.ctl

import io.koosha.foobar.maker.api.svc.PrometheusScrapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/actuator/prometheus")
class PrometheusController(
    private val scrapper: PrometheusScrapper,
) {

    @ResponseBody
    @GetMapping(produces = ["text/plain;version=0.0.4;charset=utf-8"])
    fun get(): String = this.scrapper.getLastRead()

}
