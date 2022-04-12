package io.koosha.foobar.marketplace_engine.api.cfg.prop

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
    var marketplaceUrl: String? = null

    @field:Min(1)
    @field:Max(65535)
    @field:NotNull
    var marketplacePort: Int? = null


    fun marketplaceAddress(): String = this.replacePort(this.marketplaceUrl!!, this.marketplacePort!!)


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

        return this.marketplaceUrl == other.marketplaceUrl
                && this.marketplacePort == other.marketplacePort
    }

    override fun hashCode(): Int {

        var result = 31 + (this.marketplaceUrl?.hashCode() ?: 0)
        result = 31 * result + (this.marketplacePort ?: 0)
        return result
    }

    override fun toString(): String =
        this.javaClass.name +
                "(" +
                "marketplaceUrl=$marketplaceUrl, " +
                "marketplacePort=$marketplacePort" +
                ")"

}