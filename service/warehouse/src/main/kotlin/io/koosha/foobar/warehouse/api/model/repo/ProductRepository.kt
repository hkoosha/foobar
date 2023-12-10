package io.koosha.foobar.warehouse.api.model.repo

import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.Optional
import java.util.UUID

interface ProductRepository : JpaRepository<ProductDO, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : ProductDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(id: UUID): Optional<ProductDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: ProductDO)

}
