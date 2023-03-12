package io.koosha.foobar.warehouse.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.*
import jakarta.persistence.LockModeType

interface ProductRepository : JpaRepository<ProductDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ProductDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: UUID): Optional<ProductDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ProductDO)

}
