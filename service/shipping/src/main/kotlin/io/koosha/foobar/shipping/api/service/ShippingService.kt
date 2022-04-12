package io.koosha.foobar.shipping.api.service

import io.koosha.foobar.shipping.api.model.ShippingDO
import java.util.*


interface ShippingService {

    fun findById(shippingId: UUID): Optional<ShippingDO>

    fun findByIdOrFail(shippingId: UUID): ShippingDO

    fun findAll(): Iterable<ShippingDO>

    fun create(request: ShippingCreateRequest): ShippingDO

    fun update(
        shippingId: UUID,
        request: ShippingUpdateRequest,
    ): ShippingDO

    fun delete(shippingId: UUID)

}
