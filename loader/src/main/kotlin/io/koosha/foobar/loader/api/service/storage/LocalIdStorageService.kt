package io.koosha.foobar.loader.api.service.storage

import io.koosha.foobar.loader.api.service.Rand
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock


@Service
class LocalIdStorageService(
    private val rand: Rand,
) : IdStorageService {

    companion object {

        private const val OP_GENERATE_AVAILABILITY = "generate_availability"
        private const val OP_GENERATE_AVAILABILITY_PRODUCT = "${OP_GENERATE_AVAILABILITY}_product"
        private const val OP_GENERATE_AVAILABILITY_SELLER = "${OP_GENERATE_AVAILABILITY}_seller"

    }

    private val customerLock = ReentrantReadWriteLock()
    private val sellerLock = ReentrantReadWriteLock()
    private val productLock = ReentrantReadWriteLock()
    private val orderRequestLock = ReentrantReadWriteLock()

    private val customers = mutableListOf<UUID>()
    private val sellers = mutableListOf<UUID>()
    private val products = mutableListOf<UUID>()
    private val orderRequests = mutableListOf<UUID>()

    private val cursors = ConcurrentHashMap<String, AtomicInteger>()
    private val sellerProductCursor = ConcurrentHashMap<UUID, AtomicInteger>(10_000)


    override fun addCustomer(customerId: UUID): Unit = this.customerLock.writeLock().withLock {
        this.customers.add(customerId)
    }


    override fun getRandomCustomer(): UUID = this.customerLock.readLock().withLock {
        val index = this.rand.int(this.customers.size)
        this.customers[index]
    }

    override fun getLastCustomer(): UUID = this.customerLock.readLock().withLock {
        this.customers.last()
    }

    override fun getCustomerId(index: Int): UUID? = this.customerLock.readLock().withLock {
        if (index < this.customers.size)
            this.customers[index]
        else
            null
    }

    override fun getCustomerRange(): IntRange = this.customerLock.readLock().withLock {
        0 until this.cursors.size
    }


    override fun addSeller(sellerId: UUID): Unit = this.sellerLock.writeLock().withLock {
        this.sellers.add(sellerId)
    }

    override fun getRandomSeller(): UUID = this.sellerLock.readLock().withLock {
        val index = this.rand.int(this.sellers.size)
        this.sellers[index]
    }

    override fun getLastSeller(): UUID = this.sellerLock.readLock().withLock {
        this.sellers.last()
    }

    override fun getSellerId(index: Int): UUID? = this.sellerLock.readLock().withLock {
        if (index < this.sellers.size)
            this.sellers[index]
        else
            null
    }

    override fun getSellerRange(): IntRange = this.sellerLock.readLock().withLock {
        0 until this.sellers.size
    }

    override fun addProduct(productId: UUID): Unit = this.productLock.writeLock().withLock {
        this.products.add(productId)
    }

    override fun getRandomProduct(): UUID = this.productLock.readLock().withLock {
        val index = this.rand.int(this.products.size)
        this.products[index]
    }

    override fun getLastProduct(): UUID = this.productLock.readLock().withLock {
        this.products.last()
    }

    override fun getProductId(index: Int): UUID? = this.productLock.readLock().withLock {
        if (index < this.products.size)
            this.products[index]
        else
            null
    }

    override fun getProductRange(): IntRange = this.productLock.readLock().withLock {
        0 until this.products.size
    }

    override fun getRandomProducts(count: Int): Set<UUID> = this.productLock.readLock().withLock {
        if (count <= this.products.size)
            throw IllegalArgumentException(
                "requested number of products=$count, actual available products=${this.products.size}"
            )

        val selected = mutableSetOf<UUID>()

        while (selected.size < count)
            selected.add(this.rand.select(this.products))

        return selected
    }

    override fun addOrderRequest(orderRequestId: UUID): Unit = this.orderRequestLock.writeLock().withLock {
        this.orderRequests.add(orderRequestId)
    }

    override fun getRandomOrderRequest(): UUID = this.orderRequestLock.readLock().withLock {
        val index = this.rand.int(this.orderRequests.size)
        this.orderRequests[index]
    }

    override fun getLastOrderRequest(): UUID = this.orderRequestLock.readLock().withLock {
        this.orderRequests.last()
    }

    override fun getOrderRequestId(index: Int): UUID? = this.orderRequestLock.readLock().withLock {
        if (index < this.orderRequests.size)
            this.orderRequests[index]
        else
            null
    }

    // =========================================================================

    override fun getSellerProductCursor(sellerId: UUID): Int =
        this.sellerProductCursor[sellerId]?.get()
            ?: throw NoSuchElementException("seller product cursor, sellerId=$sellerId")

    override fun incProductCursorOfGenerateAvailability(sellerId: UUID, expectedValue: Int): Int =
        (this.sellerProductCursor[sellerId]
            ?: throw NoSuchElementException("seller product cursor, sellerId=$sellerId"))
            .compareAndExchange(expectedValue, expectedValue + 1)

    // =========================================================================

    override fun getProductCursorOfGenerateAvailability(): Int =
        this.getCursor(OP_GENERATE_AVAILABILITY_PRODUCT)

    override fun incProductCursorOfGenerateAvailability(expectedValue: Int): Int =
        this.incCursor(OP_GENERATE_AVAILABILITY_PRODUCT, expectedValue)

    override fun getSellerCursorGenerateAvailability(): Int =
        this.getCursor(OP_GENERATE_AVAILABILITY_SELLER)

    override fun incSellerCursorGenerateAvailability(expectedValue: Int): Int =
        this.incCursor(OP_GENERATE_AVAILABILITY_SELLER, expectedValue)

    // =========================================================================

    private fun getCursor(name: String): Int {

        val cursor = this.cursors[name] ?: throw NoSuchElementException("cursor: $name")
        return cursor.get()
    }

    private fun incCursor(name: String, expectedValue: Int): Int {

        val cursor = this.cursors[name] ?: throw NoSuchElementException("cursor: $name")
        return cursor.compareAndExchange(expectedValue, expectedValue + 1)
    }

}
