package io.koosha.foobar.marketplace_engine.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.persistence.LockModeType


interface AvailabilityRepository : JpaRepository<AvailabilityDO, AvailabilityDO.Pk> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : AvailabilityDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: AvailabilityDO.Pk): Optional<AvailabilityDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: AvailabilityDO)

    @Lock(LockModeType.OPTIMISTIC)
    @Query(
        "SELECT a FROM AvailabilityDO a " +
                "WHERE a.availabilityPk.productId = :productId " +
                "AND a.unitsAvailable >= :unitsAvailable"
    )
    fun findAllByProductIdAndUnitsAvailableGreaterThanEqual(
        productId: UUID,
        unitsAvailable: Long?,
    ): List<AvailabilityDO>

    @Lock(LockModeType.OPTIMISTIC)
    @Query(
        "SELECT a FROM AvailabilityDO a " +
                "WHERE a.availabilityPk.productId = :productId " +
                "AND a.availabilityPk.sellerId = :sellerId"
    )
    fun findAllByproductIdAndSellerId(
        productId: UUID,
        sellerId: UUID?,
    ): Optional<AvailabilityDO>

}
