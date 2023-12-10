package io.koosha.foobar.marketplaceengine.api.model.repo

import io.koosha.foobar.marketplaceengine.api.model.entity.AvailabilityDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface AvailabilityRepository : JpaRepository<AvailabilityDO, AvailabilityDO.AvailabilityPk> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : AvailabilityDO> save(
        entity: S,
    ): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(
        id: AvailabilityDO.AvailabilityPk,
    ): Optional<AvailabilityDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(
        entity: AvailabilityDO,
    )

    @Lock(LockModeType.OPTIMISTIC)
    @Query(
        """
        SELECT a FROM AvailabilityDO a 
        WHERE a.availabilityPk.productId = :productId
        AND a.unitsAvailable >= :unitsAvailable
        """
    )
    fun findAllByProductIdAndUnitsAvailableGreaterThanEqual(
        productId: UUID,
        unitsAvailable: Long?,
    ): List<AvailabilityDO>

    @Lock(LockModeType.OPTIMISTIC)
    @Query(
        """
        SELECT a FROM AvailabilityDO a 
        WHERE a.availabilityPk.productId = :productId 
        AND a.availabilityPk.sellerId = :sellerId
        """
    )
    fun findByproductIdAndSellerId(
        productId: UUID,
        sellerId: UUID?,
    ): Optional<AvailabilityDO>

}
