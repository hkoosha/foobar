package io.koosha.foobar.shipping.api.model

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType


interface ShippingRepository : CrudRepository<ShippingDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ShippingDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<ShippingDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ShippingDO)

}
