package io.koosha.foobar.marketplace.api.model

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType


interface OrderRequestRepository : CrudRepository<OrderRequestDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : OrderRequestDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<OrderRequestDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: OrderRequestDO)

    fun findAllByCustomerId(customerId: UUID): Iterable<OrderRequestDO>

}
