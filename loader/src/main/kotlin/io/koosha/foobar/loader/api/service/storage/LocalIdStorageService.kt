package io.koosha.foobar.loader.api.service.storage

import io.koosha.foobar.loader.api.service.Rand
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock


@Service
class LocalIdStorageService(
    private val rand: Rand,
) : IdStorageService {

    private val customerLock = ReentrantReadWriteLock()
    private val sellerLock = ReentrantReadWriteLock()
    private val productLock = ReentrantReadWriteLock()
    private val orderRequestLock = ReentrantReadWriteLock()

    private val customers = mutableListOf<UUID>()
    private val sellers = mutableListOf<UUID>()
    private val products = mutableListOf<UUID>()
    private val orderRequests = mutableListOf<UUID>()


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
        0 until this.customers.size
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
        if (count >= this.products.size)
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

}
