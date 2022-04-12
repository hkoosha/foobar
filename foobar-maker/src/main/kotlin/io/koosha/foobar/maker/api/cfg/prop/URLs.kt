package io.koosha.foobar.maker.api.cfg.prop

import io.koosha.foobar.maker.api.conv.UrlOrEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull


@ConfigurationProperties("foobar.url")
@Validated
class URLs {

    @field:NotNull
    @field:UrlOrEmpty
    lateinit var defaultUrl: String

    @field:UrlOrEmpty
    var marketplace: String? = null

    @field:UrlOrEmpty
    var warehouse: String? = null

    @field:UrlOrEmpty
    var shipping: String? = null

    @field:UrlOrEmpty
    var customer: String? = null

    @field:UrlOrEmpty
    var seller: String? = null


    fun marketplace(): String =
        if (this.marketplace == null || this.marketplace!!.trim().isEmpty())
            this.defaultUrl.trim()
        else
            this.marketplace!!.trim()

    fun warehouse(): String =
        if (this.warehouse == null || this.warehouse!!.trim().isEmpty())
            this.defaultUrl.trim()
        else
            this.warehouse!!.trim()

    fun shipping(): String =
        if (this.shipping == null || this.shipping!!.trim().isEmpty())
            this.defaultUrl.trim()
        else
            this.shipping!!.trim()

    fun customer(): String =
        if (this.customer == null || this.customer!!.trim().isEmpty())
            this.defaultUrl.trim()
        else
            this.customer!!.trim()

    fun seller(): String =
        if (this.seller == null || this.seller!!.trim().isEmpty())
            this.defaultUrl.trim()
        else
            this.seller!!.trim()


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as URLs

        return this.defaultUrl == other.defaultUrl
                && this.marketplace == other.marketplace
                && this.warehouse == other.warehouse
                && this.shipping == other.shipping
                && this.customer == other.customer
                && this.seller == other.seller
    }

    override fun hashCode(): Int {
        var result = this.defaultUrl.hashCode()
        result = 31 * result + (this.marketplace?.hashCode() ?: 0)
        result = 31 * result + (this.warehouse?.hashCode() ?: 0)
        result = 31 * result + (this.shipping?.hashCode() ?: 0)
        result = 31 * result + (this.customer?.hashCode() ?: 0)
        result = 31 * result + (this.seller?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        this.javaClass.name +
                "(" +
                "base=${this.defaultUrl}, " +
                "marketplace=${this.marketplace}, " +
                "warehouse=${this.warehouse}, " +
                "shipping=${this.shipping}, " +
                "customer=${this.customer}, " +
                "seller=${this.seller}" +
                ")"

}