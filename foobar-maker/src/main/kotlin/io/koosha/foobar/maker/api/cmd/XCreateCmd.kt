package io.koosha.foobar.maker.api.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.matches
import mu.KotlinLogging
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

    private val log = KotlinLogging.logger {}

    @Suppress("SpellCheckingInspection")
    override val commandName: String = "xcreate"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {
        for (entityType in freeArgs) {
            when {
                matches(this.customerCmd.commandName, entityType) -> this.customerCmd.handle(args, emptyList())

                matches(this.addressCmd.commandName, entityType) -> this.addressCmd.handle(args, emptyList())

                matches(this.sellerCmd.commandName, entityType) -> this.sellerCmd.handle(args, emptyList())

                matches(this.availabilityCmd.commandName, entityType) -> this.availabilityCmd.handle(args, emptyList())

                matches(this.productCmd.commandName, entityType) -> this.productCmd.handle(args, emptyList())

                matches(this.orderRequestCmd.commandName, entityType) -> this.orderRequestCmd.handle(args, emptyList())

                matches(this.lineItemCmd.commandName, entityType) -> this.lineItemCmd.handle(args, emptyList())

                else -> log.error { "unknown entity type: $entityType" }
            }
        }
    }

}
