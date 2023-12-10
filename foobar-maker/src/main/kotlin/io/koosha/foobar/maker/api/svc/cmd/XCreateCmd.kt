package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.matches
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class XCreateCmd(
    private val customerCmd: CustomerCmd,
    private val addressCmd: AddressCmd,
    private val sellerCmd: SellerCmd,
    private val availabilityCmd: AvailabilityCmd,
    private val productCmd: ProductCmd,
    private val orderRequestCmd: OrderRequestCmd,
    private val lineItemCmd: LineItemCmd,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Suppress("SpellCheckingInspection")
    override val commandName: String = "xcreate"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {
        for (arg in freeArgs) {

            if (arg.contains(':')) {
                val split: List<String> = arg.split(':')
                val entityType: String = split.first()
                val xArgs: List<String> = split.subList(1, split.size)
                this.handle0(args, entityType, xArgs)
            }
            else {
                this.handle0(args, arg, emptyList())
            }

        }
    }

    private fun handle0(
        args: ApplicationArguments,
        entityType: String,
        xArgs: List<String>,
    ) {
        when {
            matches(this.customerCmd.commandName, entityType) -> this.customerCmd.handle(args, xArgs)

            matches(this.addressCmd.commandName, entityType) -> this.addressCmd.handle(args, xArgs)

            matches(this.sellerCmd.commandName, entityType) -> this.sellerCmd.handle(args, xArgs)

            matches(this.availabilityCmd.commandName, entityType) -> this.availabilityCmd.handle(args, xArgs)

            matches(this.productCmd.commandName, entityType) -> this.productCmd.handle(args, xArgs)

            matches(this.orderRequestCmd.commandName, entityType) -> this.orderRequestCmd.handle(args, xArgs)

            matches(this.lineItemCmd.commandName, entityType) -> this.lineItemCmd.handle(args, xArgs)

            else -> log.error("unknown entity type: {}", entityType)
        }
    }

}
