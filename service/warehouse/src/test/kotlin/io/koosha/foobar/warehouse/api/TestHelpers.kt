package io.koosha.foobar.warehouse.api

import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.service.AvailabilityCreateRequest
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.EntityManager


val NOW: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)

val THEN: Instant = NOW.plusDays(42).toInstant()


private val product0Id = UUID.fromString("00000000-0000-0000-0000-000000000000")
private val product1Id = UUID.fromString("00000000-0000-0000-0000-000000000001")
private val product2Id = UUID.fromString("00000000-0000-0000-0000-000000000002")
private val seller0Id = UUID.fromString("00000000-0000-0000-0000-000000000003")
private val seller1Id = UUID.fromString("00000000-0000-0000-0000-000000000004")
private val seller2Id = UUID.fromString("00000000-0000-0000-0000-000000000005")


fun product0() = ProductDO(
    productId = product0Id,
    version = null,
    created = NOW,
    updated = NOW,
    name = "prod0",
    unitSingle = "kg",
    unitMultiple = "kg",
    active = true
)

fun product1() = ProductDO(
    productId = product1Id,
    version = null,
    created = NOW,
    updated = NOW,
    name = "prod1",
    unitSingle = "mm",
    unitMultiple = "mm",
    active = false
)

fun product2() = ProductDO(
    productId = product2Id,
    version = null,
    created = NOW,
    updated = NOW,
    name = "prod2",
    unitSingle = "cm",
    unitMultiple = "cm",
    active = true
)

fun availabilityReq0() = AvailabilityCreateRequest(
    sellerId = seller0Id,
    unitsAvailable = 42000L,
    pricePerUnit = 10_000,
)

fun availabilityReq1() = AvailabilityCreateRequest(
    sellerId = seller1Id,
    unitsAvailable = 42420L,
    pricePerUnit = 80_000L,
)

fun availabilityReq2() = AvailabilityCreateRequest(
    sellerId = seller2Id,
    unitsAvailable = 2400L,
    pricePerUnit = 10L,
)


val IDS = setOf(
    product0().productId!!,
    product1().productId!!,
    product2().productId!!,
    availabilityReq0().sellerId,
    availabilityReq1().sellerId,
    availabilityReq2().sellerId,
)

// =============================================================================

fun strOfLen(len: Int): String {
    val sb = StringBuilder(len)
    for (i in 0 until len)
        sb.append('x')
    Assertions.assertThat(sb).hasSize(len)
    return sb.toString()
}

fun <T> anyObject(type: Class<T>): T = Mockito.any(type)

fun TransactionTemplate.inTx(em: EntityManager, action: (EntityManager) -> Unit) {
    this.execute(object : TransactionCallbackWithoutResult() {
        override fun doInTransactionWithoutResult(status: TransactionStatus) {
            action(em)
        }
    })
}
