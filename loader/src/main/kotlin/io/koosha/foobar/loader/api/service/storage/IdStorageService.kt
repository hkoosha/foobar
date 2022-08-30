package io.koosha.foobar.loader.api.service.storage

import java.util.*


interface IdStorageService {

    fun addCustomer(customerId: UUID)

    fun getRandomCustomer(): UUID

    fun getLastCustomer(): UUID

    fun getCustomerId(index: Int): UUID?

    fun getCustomerRange(): IntRange


    fun addSeller(sellerId: UUID)

    fun getRandomSeller(): UUID

    fun getLastSeller(): UUID

    fun getSellerId(index: Int): UUID?

    fun getSellerRange(): IntRange


    fun addProduct(productId: UUID)

    fun getRandomProduct(): UUID

    fun getLastProduct(): UUID

    fun getProductId(index: Int): UUID?

    fun getProductRange(): IntRange

    fun getRandomProducts(count: Int): Set<UUID>


    fun addOrderRequest(orderRequestId: UUID)

    fun getRandomOrderRequest(): UUID

    fun getLastOrderRequest(): UUID

    fun getOrderRequestId(index: Int): UUID?

    // =========================================================================

    fun getSellerProductCursor(sellerId: UUID): Int

    fun incProductCursorOfGenerateAvailability(sellerId: UUID, expectedValue: Int): Int

    // =========================================================================

    fun getProductCursorOfGenerateAvailability(): Int

    fun incProductCursorOfGenerateAvailability(expectedValue: Int): Int

    fun getSellerCursorGenerateAvailability(): Int

    fun incSellerCursorGenerateAvailability(expectedValue: Int): Int

}
