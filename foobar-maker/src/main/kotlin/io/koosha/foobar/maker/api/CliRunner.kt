package io.koosha.foobar.maker.api

import io.koosha.foobar.maker.api.svc.cmd.AddressCmd
import io.koosha.foobar.maker.api.svc.cmd.AvailabilityCmd
import io.koosha.foobar.maker.api.svc.cmd.CustomerCmd
import io.koosha.foobar.maker.api.svc.cmd.DumpCmd
import io.koosha.foobar.maker.api.svc.cmd.InitCmd
import io.koosha.foobar.maker.api.svc.cmd.LineItemCmd
import io.koosha.foobar.maker.api.svc.cmd.LoopCmd
import io.koosha.foobar.maker.api.svc.cmd.OrderRequestCmd
import io.koosha.foobar.maker.api.svc.cmd.ProductCmd
import io.koosha.foobar.maker.api.svc.cmd.SellerCmd
import io.koosha.foobar.maker.api.svc.cmd.XCreateCmd
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.system.exitProcess


@Component
class CliRunner(
    private val customerCmd: CustomerCmd,
    private val addressCmd: AddressCmd,
    private val sellerCmd: SellerCmd,
    private val availabilityCmd: AvailabilityCmd,
    private val productCmd: ProductCmd,
    private val orderRequestCmd: OrderRequestCmd,
    private val lineItemCmd: LineItemCmd,
    private val initCommand: InitCmd,
    private val dumpCmd: DumpCmd,
    private val xCreateCmd: XCreateCmd,
    private val loopCmd: LoopCmd,
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun run(args: ApplicationArguments) {

        try {
            this.tryRun(args)
        }
        catch (ex: CliException) {
            log.error { ex.message }
            exitProcess(1)
        }
        catch (ex: feign.FeignException) {
            log.error { ex.message }
            exitProcess(1)
        }
    }

    private fun tryRun(args: ApplicationArguments) = when {
        args.nonOptionArgs.size == 0 -> throw CliException("expecting a command, got none")

        matches(this.initCommand.commandName, args.nonOptionArgs.first()) -> this.initCommand.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.dumpCmd.commandName, args.nonOptionArgs.first()) -> this.dumpCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.customerCmd.commandName, args.nonOptionArgs.first()) -> this.customerCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.addressCmd.commandName, args.nonOptionArgs.first()) -> this.addressCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.sellerCmd.commandName, args.nonOptionArgs.first()) -> this.sellerCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.productCmd.commandName, args.nonOptionArgs.first()) -> this.productCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.availabilityCmd.commandName, args.nonOptionArgs.first()) -> this.availabilityCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.orderRequestCmd.commandName, args.nonOptionArgs.first()) -> this.orderRequestCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.lineItemCmd.commandName, args.nonOptionArgs.first()) -> this.lineItemCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.xCreateCmd.commandName, args.nonOptionArgs.first()) -> this.xCreateCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        matches(this.loopCmd.commandName, args.nonOptionArgs.first()) -> this.loopCmd.handle(
            args,
            args.nonOptionArgs.subList(1, args.nonOptionArgs.size),
        )

        else -> throw CliException("unknown command: ${args.nonOptionArgs.first()}")
    }

}
