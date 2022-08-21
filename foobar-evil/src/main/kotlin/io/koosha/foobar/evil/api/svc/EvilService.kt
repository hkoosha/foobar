package io.koosha.foobar.evil.api.svc

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class EvilService
// (
// private val serviceAddress: ServiceAddress,
// private val client: OkHttpClient,
// )
{

    @Volatile
    var running = true

    fun run() {

        while (this.running)
            this.doRun()
    }

    private fun doRun() {

        Thread.sleep(1000)
    }

}
