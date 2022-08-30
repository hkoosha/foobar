package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.cfg.prop.Ports
import io.koosha.foobar.maker.api.cfg.prop.URLs
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component


@Component
class DumpCmd(
    private val ports: Ports,
    private val urls: URLs,
) : Command {

    private val log = KotlinLogging.logger {}


    override val commandName = "dump"


    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        log.info { "ports: $ports" }
        log.info { "urls: $urls" }
        log.info { "customer: ${urls.customer()}" }
        log.info { "seller: ${urls.seller()}" }
        log.info { "warehouse: ${urls.warehouse()}" }
        log.info { "marketplace: ${urls.marketplace()}" }
        log.info { "shipping: ${urls.shipping()}" }
    }

}
