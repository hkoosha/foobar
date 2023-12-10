package io.koosha.foobar.seller.api.model.repo

import io.koosha.foobar.seller.api.model.entity.SellerDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface SellerRepository : CrudRepository<SellerDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : SellerDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(id: UUID): Optional<SellerDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: SellerDO)

}
