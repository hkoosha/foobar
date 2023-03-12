package io.koosha.foobar.customer.api.model

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*


interface CustomerRepository : CrudRepository<CustomerDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : CustomerDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<CustomerDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: CustomerDO)

}
