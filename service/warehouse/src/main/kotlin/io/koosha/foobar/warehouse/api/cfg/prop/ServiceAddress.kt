package io.koosha.foobar.warehouse.api.cfg.prop

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

    companion object {
        private const val PORT_MAX = 65535L
        private const val PORT_MIN = 1L
    }

    @field:PortlessUrl
    var sellerUrl: String? = null

    @field:Min(PORT_MIN)
    @field:Max(PORT_MAX)
    @field:NotNull
    var sellerPort: Int? = null


    fun sellerAddress(): String = this.replacePort(this.sellerUrl!!, this.sellerPort!!)


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

        return this.sellerUrl == other.sellerUrl
                && this.sellerPort == other.sellerPort
    }

    override fun hashCode(): Int {
        var result = this.sellerUrl?.hashCode() ?: 0
        result = 31 * result + (this.sellerPort ?: 0)
        return result
    }

    override fun toString(): String {
        return this.javaClass.name +
                "(" +
                "sellerUrl=${this.sellerUrl}, " +
                "sellerPort=${this.sellerPort}" +
                ")"
    }

}
