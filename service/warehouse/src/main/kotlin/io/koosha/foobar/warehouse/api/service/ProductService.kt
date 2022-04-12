package io.koosha.foobar.warehouse.api.service

import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.model.ProductDO
import java.util.*


interface ProductService {

    fun findAll(): Iterable<ProductDO>

    fun findById(productId: UUID): Optional<ProductDO>

    fun findByIdOrFail(productId: UUID): ProductDO

    fun create(request: ProductCreateRequest): ProductDO

    fun update(
        productId: UUID,
        request: ProductUpdateRequest,
    ): ProductDO

    fun delete(productId: UUID)

    fun addAvailability(
        productId: UUID,
        request: AvailabilityCreateRequest,
    ): AvailabilityDO

    fun deleteAvailability(
        productId: UUID,
        sellerId: UUID,
    )

    fun updateAvailability(
        productId: UUID,
        sellerId: UUID,
        request: AvailabilityUpdateRequest,
    ): AvailabilityDO

    fun getAvailabilitiesOf(productId: UUID): Iterable<AvailabilityDO>

    fun getAvailability(
        productId: UUID,
        sellerId: UUID,
    ): AvailabilityDO

}
