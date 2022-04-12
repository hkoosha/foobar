package io.koosha.foobar.marketplace.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.*
import javax.persistence.LockModeType


interface ProcessedOrderRequestSellerRepository : JpaRepository<ProcessedOrderRequestSellerDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ProcessedOrderRequestSellerDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<ProcessedOrderRequestSellerDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ProcessedOrderRequestSellerDO)

}
