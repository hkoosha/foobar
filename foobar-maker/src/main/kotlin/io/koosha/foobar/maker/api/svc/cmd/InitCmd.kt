package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.firstOrDef
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class InitCmd(
    private val customerCmd: CustomerCmd,
    private val addressCmd: AddressCmd,
    private val sellerCmd: SellerCmd,
    private val availabilityCmd: AvailabilityCmd,
    private val productCmd: ProductCmd,
    private val orderRequestCmd: OrderRequestCmd,
    private val lineItemCmd: LineItemCmd,
) : Command {

    override val commandName = "init"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val magic = args.firstOrDef("magic", "1").toInt()
        val subMagic = args.firstOrDef("sub-magic", "3").toInt()

        for (i in 0 until magic) {
            this.customerCmd.handle(args, emptyList())
            repeat(subMagic) {
                this.addressCmd.handle(args, emptyList())
            }
        }

        for (i in 0 until magic)
            this.sellerCmd.handle(args, emptyList())

        for (i in 0 until magic) {
            this.productCmd.handle(args, emptyList())
            if (i % 2 == 0)
                this.availabilityCmd.handle(args, emptyList())
        }

        for (i in 0 until magic) {
            this.orderRequestCmd.handle(args, emptyList())
            this.lineItemCmd.handle(args, emptyList())
        }

        Thread.sleep(500)
        if (args.containsOption("live"))
            this.orderRequestCmd.patchOrderRequest()

        Thread.sleep(500)
        this.orderRequestCmd.getOrderRequest(emptyList())
        this.lineItemCmd.getLineItem(emptyList())
    }

}
