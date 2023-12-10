package io.koosha.foobar.marketplaceengine.api.model.repo

import io.koosha.foobar.marketplaceengine.api.model.entity.ProcessedOrderRequestDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.Optional
import java.util.UUID

interface ProcessedOrderRequestRepository : JpaRepository<ProcessedOrderRequestDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ProcessedOrderRequestDO> save(
        entity: S,
    ): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(
        id: UUID,
    ): Optional<ProcessedOrderRequestDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(
        entity: ProcessedOrderRequestDO,
    )

}
