package io.koosha.foobar.marketplaceengine.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.*
import jakarta.persistence.LockModeType


interface ProcessedOrderRequestRepository : JpaRepository<ProcessedOrderRequestDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ProcessedOrderRequestDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<ProcessedOrderRequestDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ProcessedOrderRequestDO)

}
