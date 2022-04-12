package io.koosha.foobar.seller.api.model

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType


interface SellerRepository : CrudRepository<SellerDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : SellerDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<SellerDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: SellerDO)

}
