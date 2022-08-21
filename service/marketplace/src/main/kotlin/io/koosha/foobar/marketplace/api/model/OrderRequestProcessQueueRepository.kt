package io.koosha.foobar.marketplace.api.model

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType


interface OrderRequestProcessQueueRepository :
    CrudRepository<OrderRequestProcessQueueDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : OrderRequestProcessQueueDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<OrderRequestProcessQueueDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: OrderRequestProcessQueueDO)

}
