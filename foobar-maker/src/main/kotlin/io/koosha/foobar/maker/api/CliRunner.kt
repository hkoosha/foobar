package io.koosha.foobar.maker.api

import io.koosha.foobar.maker.api.cmd.AddressCmd
import io.koosha.foobar.maker.api.cmd.AvailabilityCmd
import io.koosha.foobar.maker.api.cmd.CustomerCmd
import io.koosha.foobar.maker.api.cmd.DumpCmd
import io.koosha.foobar.maker.api.cmd.InitCmd
import io.koosha.foobar.maker.api.cmd.LineItemCmd
import io.koosha.foobar.maker.api.cmd.OrderRequestCmd
import io.koosha.foobar.maker.api.cmd.ProductCmd
import io.koosha.foobar.maker.api.cmd.SellerCmd
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

        else -> throw CliException("unknown command: ${args.nonOptionArgs.first()}")
    }

}
