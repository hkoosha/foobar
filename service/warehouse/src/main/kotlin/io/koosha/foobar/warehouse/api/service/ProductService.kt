package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.warehouse.api.model.dto.AvailabilityCreateRequestDto
import io.koosha.foobar.warehouse.api.model.dto.AvailabilityUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.dto.ProductCreateRequestDto
import io.koosha.foobar.warehouse.api.model.dto.ProductUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import org.springframework.stereotype.Service
import java.util.UUID

@Service
final class ProductService(
    private val finder: ProductServiceFinder,
    private val creator: ProductServiceCreator,
    private val updater: ProductServiceUpdater,
    private val deleter: ProductServiceDeleter,
    private val availabilityFinder: ProductServiceAvailabilityFinder,
    private val availabilityUpdater: ProductServiceAvailabilityUpdater,
    private val availabilityAdder: ProductServiceAvailabilityAdder,
    private val availabilityDeleter: ProductServiceAvailabilityDeleter,
) {

    fun findByIdOrFail(
        productId: UUID,
    ): ProductDO =
        this.finder.findByIdOrFail(productId)

    fun findAll(): Iterable<ProductDO> =
        this.finder.findAll()

    fun create(
        request: ProductCreateRequestDto,
    ): ProductDO =
        this.creator.create(request)

    fun update(
        productId: UUID,
        request: ProductUpdateRequestDto,
    ): ProductDO =
        this.updater.update(productId, request)

    fun delete(
        productId: UUID,
    ): Unit =
        this.deleter.delete(productId)

    fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequestDto,
    ): AvailabilityDO =
        this.availabilityAdder.addAvailability(productId, request)

    fun deleteAvailability(
        productId: UUID,
        sellerId: UUID,
    ): Unit =
        this.availabilityDeleter.deleteAvailability(productId, sellerId)

    fun getAvailabilitiesOf(
        productId: UUID,
    ): Iterable<AvailabilityDO> =
        this.availabilityFinder.getAvailabilitiesOf(productId)

    fun getAvailability(
        productId: UUID,
        sellerId: UUID,
    ): AvailabilityDO =
        this.availabilityFinder.getAvailability(productId, sellerId)

    fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequestDto,
    ): AvailabilityDO =
        this.availabilityUpdater.updateAvailability(productId, sellerId, request)

}
