package io.koosha.foobar.seller.api.service

import io.koosha.foobar.seller.api.model.SellerDO
import java.util.*


interface SellerService {

    fun findAll(): Iterable<SellerDO>

    fun findById(id: UUID): Optional<SellerDO>

    fun findByIdOrFail(id: UUID): SellerDO

    fun create(request: SellerCreateRequest): SellerDO

    fun update(
        sellerId: UUID,
        request: SellerUpdateRequest,
    ): SellerDO

    fun delete(sellerId: UUID)

}
