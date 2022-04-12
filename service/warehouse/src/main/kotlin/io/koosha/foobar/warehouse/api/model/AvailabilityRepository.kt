package io.koosha.foobar.warehouse.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.*
import javax.persistence.LockModeType


interface AvailabilityRepository : JpaRepository<AvailabilityDO, AvailabilityDO.AvailabilityPk> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : AvailabilityDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: AvailabilityDO.AvailabilityPk): Optional<AvailabilityDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: AvailabilityDO)

    @Suppress("FunctionName")
    @Lock(LockModeType.OPTIMISTIC)
    fun findByAvailabilityPk_ProductAndAvailabilityPk_SellerId(
        product: ProductDO,
        sellerId: UUID,
    ): Optional<AvailabilityDO>

    @Suppress("FunctionName")
    @Lock(LockModeType.OPTIMISTIC)
    fun findAllByAvailabilityPk_Product(
        product: ProductDO,
    ): Iterable<AvailabilityDO>

}
