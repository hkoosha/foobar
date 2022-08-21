package io.koosha.foobar.marketplace.api.model

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.LockModeType


@Suppress("FunctionName")
interface OrderRequestLineItemRepository :
    CrudRepository<OrderRequestLineItemDO, OrderRequestLineItemDO.Pk> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : OrderRequestLineItemDO> save(entity: S): S

    @Lock(LockModeType.OPTIMISTIC)
    @Suppress("SpringDataMethodInconsistencyInspection")
    override fun findById(id: OrderRequestLineItemDO.Pk): Optional<OrderRequestLineItemDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(entity: OrderRequestLineItemDO)

    @Lock(LockModeType.OPTIMISTIC)
    fun deleteAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(orderRequestId: UUID)

    @Lock(LockModeType.OPTIMISTIC)
    fun findAllByOrderRequestLineItemPk_OrderRequest_orderRequestId(
        orderRequestId: UUID,
    ): Iterable<OrderRequestLineItemDO>

}
