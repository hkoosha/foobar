package io.koosha.foobar.evil.api.cfg.prop

import io.koosha.foobar.common.conv.PortlessUrl
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI
import javax.validation.constraints.Max
import javax.validation.constraints.Min


@ConfigurationProperties("foobar.url")
@Validated
class ServiceAddress {

    @field:PortlessUrl
    var defaultUrl: String? = null

    @field:PortlessUrl(allowNull = true, allowEmpty = true)
    var marketplaceUrl: String? = null

    @field:PortlessUrl(allowNull = true, allowEmpty = true)
    var warehouseUrl: String? = null

    @field:PortlessUrl(allowNull = true, allowEmpty = true)
    var shippingUrl: String? = null

    @field:PortlessUrl(allowNull = true, allowEmpty = true)
    var customerUrl: String? = null

    @field:PortlessUrl(allowNull = true, allowEmpty = true)
    var sellerUrl: String? = null

    @field:Min(1)
    @field:Max(65535)
    var marketplacePort: Int? = null

    @field:Min(1)
    @field:Max(65535)
    var warehousePort: Int? = null

    @field:Min(1)
    @field:Max(65535)
    var shippingPort: Int? = null

    @field:Min(1)
    @field:Max(65535)
    var customerPort: Int? = null

    @field:Min(1)
    @field:Max(65535)
    var sellerPort: Int? = null


    fun customerAddress(): String = this.replacePort(
        if (this.customerUrl == null || this.customerUrl!!.isEmpty())
            this.defaultUrl!!
        else
            this.customerUrl!!,
        this.customerPort!!
    )

    fun sellerAddress(): String = this.replacePort(
        if (this.sellerUrl == null || this.sellerUrl!!.isEmpty())
            this.defaultUrl!!
        else
            this.sellerUrl!!,
        this.sellerPort!!
    )

    fun warehouseAddress(): String = this.replacePort(
        if (this.warehouseUrl == null || this.warehouseUrl!!.isEmpty())
            this.defaultUrl!!
        else
            this.warehouseUrl!!,
        this.warehousePort!!
    )

    fun marketplaceAddress(): String = this.replacePort(
        if (this.marketplaceUrl == null || this.marketplaceUrl!!.isEmpty())
            this.defaultUrl!!
        else
            this.marketplaceUrl!!,
        this.marketplacePort!!
    )

    fun shippingAddress(): String = this.replacePort(
        if (this.shippingUrl == null || this.shippingUrl!!.isEmpty())
            this.defaultUrl!!
        else
            this.shippingUrl!!,
        this.shippingPort!!
    )


    private fun replacePort(url: String, port: Int): String {

        val uri = URI(url)

        val reconstruct = URI(
            uri.scheme.lowercase(),
            uri.userInfo,
            uri.host,
            port,
            uri.path,
            uri.query,
            uri.fragment
        )

        return reconstruct.toString()
    }


    override fun equals(other: Any?): Boolean {

        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as ServiceAddress

        return this.defaultUrl == other.defaultUrl &&
                this.marketplaceUrl == other.marketplaceUrl &&
                this.warehouseUrl == other.warehouseUrl &&
                this.shippingUrl == other.shippingUrl &&
                this.customerUrl == other.customerUrl &&
                this.sellerUrl == other.sellerUrl &&
                this.marketplacePort == other.marketplacePort &&
                this.warehousePort == other.warehousePort &&
                this.shippingPort == other.shippingPort &&
                this.customerPort == other.customerPort &&
                this.sellerPort == other.sellerPort
    }

    override fun hashCode(): Int {
        var result = this.defaultUrl?.hashCode() ?: 0
        result = 31 * result + (this.marketplaceUrl?.hashCode() ?: 0)
        result = 31 * result + (this.warehouseUrl?.hashCode() ?: 0)
        result = 31 * result + (this.shippingUrl?.hashCode() ?: 0)
        result = 31 * result + (this.customerUrl?.hashCode() ?: 0)
        result = 31 * result + (this.sellerUrl?.hashCode() ?: 0)
        result = 31 * result + (this.marketplacePort ?: 0)
        result = 31 * result + (this.warehousePort ?: 0)
        result = 31 * result + (this.shippingPort ?: 0)
        result = 31 * result + (this.customerPort ?: 0)
        result = 31 * result + (this.sellerPort ?: 0)
        return result
    }

    override fun toString(): String =
        this.javaClass.name +
                "(" +
                "defaultUrl=$defaultUrl, " +
                "marketplaceUrl=$marketplaceUrl, " +
                "warehouseUrl=$warehouseUrl, " +
                "shippingUrl=$shippingUrl, " +
                "customerUrl=$customerUrl, " +
                "sellerUrl=$sellerUrl, " +
                "marketplacePort=$marketplacePort, " +
                "warehousePort=$warehousePort, " +
                "shippingPort=$shippingPort, " +
                "customerPort=$customerPort, " +
                "sellerPort=$sellerPort" +
                ")"

}
