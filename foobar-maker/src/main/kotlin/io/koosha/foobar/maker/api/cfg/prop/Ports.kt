package io.koosha.foobar.maker.api.cfg.prop

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Min


@ConfigurationProperties("foobar.port")
@Validated
class Ports {

    @field:Min(1)
    var marketplace: Int = -1

    @field:Min(1)
    var warehouse: Int = -1

    @field:Min(1)
    var shipping: Int = -1

    @field:Min(1)
    var customer: Int = -1

    @field:Min(1)
    var seller: Int = -1


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as Ports

        return this.marketplace == other.marketplace
                && this.warehouse == other.warehouse
                && this.shipping == other.shipping
                && this.customer == other.customer
                && this.seller == other.seller
    }

    override fun hashCode(): Int {
        var result = this.marketplace.hashCode()
        result = 31 * result + this.warehouse.hashCode()
        result = 31 * result + this.shipping.hashCode()
        result = 31 * result + this.customer.hashCode()
        result = 31 * result + this.seller.hashCode()
        return result
    }

    override fun toString(): String =
        this.javaClass.name +
                "(" +
                "marketplace=${this.marketplace}, " +
                "warehouse=${this.warehouse}, " +
                "shipping=${this.shipping}, " +
                "customer=${this.customer}, " +
                "seller=${this.seller}" +
                ")"

}
