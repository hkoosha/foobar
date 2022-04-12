package io.koosha.foobar.marketplace.api.config.prop

import io.koosha.foobar.common.conv.PortlessUrl
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


@ConfigurationProperties("foobar.services")
@Validated
class ServiceAddress {

    @field:PortlessUrl
    var customerUrl: String? = null

    @field:Min(1)
    @field:Max(65535)
    @field:NotNull
    var customerPort: Int? = null

    @field:PortlessUrl
    var sellerUrl: String? = null

    @field:Min(1)
    @field:Max(65535)
    @field:NotNull
    var sellerPort: Int? = null

    @field:PortlessUrl
    var warehouseUrl: String? = null

    @field:Min(1)
    @field:Max(65535)
    @field:NotNull
    var warehousePort: Int? = null


    fun customerAddress(): String = this.replacePort(this.customerUrl!!, this.customerPort!!)

    fun sellerAddress(): String = this.replacePort(this.sellerUrl!!, this.sellerPort!!)

    fun warehouseAddress(): String = this.replacePort(this.warehouseUrl!!, this.warehousePort!!)


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

        return this.customerUrl == other.customerUrl
                && this.customerPort == other.customerPort
                && this.sellerUrl == other.sellerUrl
                && this.sellerPort == other.sellerPort
                && this.warehouseUrl == other.warehouseUrl
                && this.warehousePort == other.warehousePort
    }

    override fun hashCode(): Int {

        var result = this.customerUrl?.hashCode() ?: 0
        result = 31 * result + (this.customerPort ?: 0)
        result = 31 * result + (this.sellerUrl?.hashCode() ?: 0)
        result = 31 * result + (this.sellerPort ?: 0)
        result = 31 * result + (this.warehouseUrl?.hashCode() ?: 0)
        result = 31 * result + (this.warehousePort ?: 0)
        return result
    }

    override fun toString(): String =
        this.javaClass.name +
                "(" +
                "customerUrl=$customerUrl, " +
                "customerPort=$customerPort, " +
                "sellerUrl=$sellerUrl, " +
                "sellerPort=$sellerPort, " +
                "warehouseUrl=$warehouseUrl, " +
                "warehousePort=$warehousePort" +
                ")"

}
