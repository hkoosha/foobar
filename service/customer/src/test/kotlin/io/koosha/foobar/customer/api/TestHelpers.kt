package io.koosha.foobar.customer.api

import io.koosha.foobar.customer.api.model.CustomerState
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.model.dto.CustomerAddressCreateRequestDto
import io.koosha.foobar.customer.api.model.entity.AddressDO
import io.koosha.foobar.customer.api.model.entity.CustomerDO
import org.assertj.core.api.Assertions.assertThat
import java.time.Instant
import java.util.UUID


val NOW: Instant = Instant.now()


private val customer0Id = UUID.fromString("00000000-0000-0000-0000-000000000000")
private val customer1Id = UUID.fromString("00000000-0000-0000-0000-000000000001")
private val customer2Id = UUID.fromString("00000000-0000-0000-0000-000000000002")

fun customer0() = CustomerDO(
    customerId = customer0Id,
    version = null,
    created = NOW,
    updated = NOW,
    state = CustomerState.ACTIVE,
    name = CustomerDO.NameDO(
        title = Title.NOT_SPECIFIED,
        firstName = "donkey",
        lastName = "monkey",
    ),
    addressIdPool = 0L,
)

fun customer1() = CustomerDO(
    customerId = customer1Id,
    version = null,
    created = NOW,
    updated = NOW,
    state = CustomerState.MARKED_FOR_REMOVAL,
    name = CustomerDO.NameDO(
        title = Title.NOT_SPECIFIED,
        firstName = "rabbit",
        lastName = "snake",
    ),
    addressIdPool = 0L,
)

fun customer2() = CustomerDO(
    customerId = customer2Id,
    version = null,
    created = NOW,
    updated = NOW,
    state = CustomerState.ACTIVE,
    name = CustomerDO.NameDO(
        title = Title.NOT_SPECIFIED,
        firstName = "elephant",
        lastName = "crocodile",
    ),
    addressIdPool = 0L,
)

val IDS = setOf(
    customer0().customerId!!,
    customer1().customerId!!,
    customer2().customerId!!,
)

fun addressReq0() = CustomerAddressCreateRequestDto(
    "40211",
    "Germany",
    "Frankfurt",
    "Porschestrasse 23",
    "home address"
)

fun addressReq1() = CustomerAddressCreateRequestDto(
    "11222",
    "Germany",
    "Berlin",
    "Berliner 45",
    "work address"
)

fun addressDO0() = AddressDO(
    addressPk = AddressDO.AddressPk(
        addressId = 3,
        customer = customer0(),
    ),
    version = null,
    created = null,
    updated = null,
    name = addressReq0().name,
    zipcode = addressReq0().zipcode,
    addressLine1 = addressReq0().addressLine1,
    country = addressReq0().country,
    city = addressReq0().city,
)

fun addressDO1() = AddressDO(
    addressPk = AddressDO.AddressPk(
        addressId = 4,
        customer = customer0(),
    ),
    version = null,
    created = null,
    updated = null,
    name = addressReq0().name,
    zipcode = addressReq0().zipcode,
    addressLine1 = addressReq0().addressLine1,
    country = addressReq0().country,
    city = addressReq0().city,
)


fun strOfLen(len: Int): String {
    val sb = StringBuilder(len)
    repeat(len) {
        sb.append('x')
    }
    assertThat(sb).hasSize(len)
    return sb.toString()
}
