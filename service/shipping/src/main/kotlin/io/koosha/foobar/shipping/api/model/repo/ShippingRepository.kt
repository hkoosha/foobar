package io.koosha.foobar.shipping.api.model.repo

import io.koosha.foobar.shipping.api.model.entity.ShippingDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface ShippingRepository : CrudRepository<ShippingDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ShippingDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(id: UUID): Optional<ShippingDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ShippingDO)

}
