package io.koosha.foobar.shipping.api.model

import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.Size

@Embeddable
open class AddressDO(

    @Column(
        // name = "ZIP_CODE",
        nullable = false,
    )
    @Size(max = 127)
    open var zipcode: String? = null,

    @Column(
        // name = "ADDRESS_LINE_1",
        nullable = false
    )
    @Size(max = 127)
    open var addressLine1: String? = null,

    @Column(
        // name = "COUNTRY",
        nullable = false
    )
    @Size(max = 127)
    open var country: String? = null,

    @Column(
        // name = "CITY",
        nullable = false
    )
    @Size(max = 127)
    open var city: String? = null,

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as AddressDO
        return this.zipcode == rhs.zipcode
                && this.addressLine1 == rhs.addressLine1
                && this.country == rhs.country
                && this.city == rhs.city
    }

    override fun hashCode(): Int = Objects.hash(
        this.zipcode,
        this.addressLine1,
        this.country,
        this.city,
    )

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "addressLine1=" + this.addressLine1 +
            ", city=" + this.city +
            ", zipCode=" + this.zipcode +
            ", country=" + this.country +
            ")"

}