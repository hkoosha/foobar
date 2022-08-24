package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.ProductDO
import org.springframework.stereotype.Service
import java.util.*


@Service
final class ProductServiceImpl(
    private val finder: ProductServiceFinderImpl,
    private val creator: ProductServiceCreatorImpl,
    private val updater: ProductServiceUpdaterImpl,
    private val deleter: ProductServiceDeleterImpl,
    private val availabilityFinder: ProductServiceAvailabilityFinderImpl,
    private val availabilityUpdater: ProductServiceAvailabilityUpdaterImpl,
    private val availabilityAdder: ProductServiceAvailabilityAdderImpl,
    private val availabilityDeleter: ProductServiceAvailabilityDeleterImpl,
) : ProductService {

    override fun findById(productId: UUID): Optional<ProductDO> = this.finder.findById(productId)

    override fun findByIdOrFail(productId: UUID): ProductDO = this.finder.findByIdOrFail(productId)

    override fun findAll(): Iterable<ProductDO> = this.finder.findAll()

    override fun create(request: ProductCreateRequest): ProductDO = this.creator.create(request)

    override fun update(
        productId: UUID,
        request: ProductUpdateRequest,
    ): ProductDO = this.updater.update(productId, request)

    override fun delete(productId: UUID) = this.deleter.delete(productId)

    override fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequest,
    ): AvailabilityDO = this.availabilityAdder.addAvailability(productId, request)

    override fun deleteAvailability(
        productId: UUID,
        sellerId: UUID,
    ) = this.availabilityDeleter.deleteAvailability(productId, sellerId)

    override fun getAvailabilitiesOf(productId: UUID): Iterable<AvailabilityDO> =
        this.availabilityFinder.getAvailabilitiesOf(productId)

    override fun getAvailability(productId: UUID, sellerId: UUID): AvailabilityDO =
        this.availabilityFinder.getAvailability(productId, sellerId)

    override fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ): AvailabilityDO = this.availabilityUpdater.updateAvailability(productId, sellerId, request)

}
