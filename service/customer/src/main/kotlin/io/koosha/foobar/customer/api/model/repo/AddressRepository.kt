package io.koosha.foobar.customer.api.model.repo

import io.koosha.foobar.customer.api.model.entity.AddressDO
import io.koosha.foobar.customer.api.model.entity.CustomerDO
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

@Suppress("FunctionName")
interface AddressRepository : CrudRepository<AddressDO, AddressDO.AddressPk> {

    @Lock(LockModeType.OPTIMISTIC)
    override fun <S : AddressDO> save(
        entity: S,
    ): S

    @Lock(LockModeType.OPTIMISTIC)
    override fun findById(
        id: AddressDO.AddressPk,
    ): Optional<AddressDO>

    @Lock(LockModeType.OPTIMISTIC)
    override fun delete(
        entity: AddressDO,
    )

    @Lock(LockModeType.OPTIMISTIC)
    fun deleteByAddressPk_Customer_customerId(
        customerId: UUID,
    )

    @Lock(LockModeType.OPTIMISTIC)
    fun findByAddressPk(
        addressPk: AddressDO.AddressPk,
    ): Optional<AddressDO>

    @Lock(LockModeType.OPTIMISTIC)
    fun findByAddressPk_Customer(
        customer: CustomerDO,
    ): Iterable<AddressDO>

    @Lock(LockModeType.OPTIMISTIC)
    fun findByAddressPk_Customer_customerIdAndAddressPk_addressId(
        customerId: UUID,
        addressId: Long,
    ): Optional<AddressDO>

}
