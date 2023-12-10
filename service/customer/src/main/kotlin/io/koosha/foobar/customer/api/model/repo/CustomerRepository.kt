package io.koosha.foobar.customer.api.model.repo

import io.koosha.foobar.customer.api.model.entity.CustomerDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface CustomerRepository : CrudRepository<CustomerDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : CustomerDO> save(
        entity: S,
    ): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(
        id: UUID,
    ): Optional<CustomerDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(
        entity: CustomerDO,
    )

}
