package io.koosha.foobar.customer.api.service

import io.koosha.foobar.customer.api.model.AddressDO
import io.koosha.foobar.customer.api.model.CustomerDO
import java.util.*


interface CustomerService {

    fun findAll(): Iterable<CustomerDO>

    fun findById(customerId: UUID): Optional<CustomerDO>

    fun findByCustomerIdOrFail(customerId: UUID): CustomerDO

    fun create(request: CustomerCreateRequest): CustomerDO

    fun update(
        customerId: UUID,
        request: CustomerUpdateRequest,
    ): CustomerDO

    fun delete(customerId: UUID)

    fun addAddress(
        customerId: UUID,
        request: CustomerAddressCreateRequest,
    ): AddressDO

    fun deleteAddress(
        customerId: UUID,
        addressId: Long,
    )

    fun getAddressesOfCustomer(
        customerId: UUID,
    ): Iterable<AddressDO>

    fun getAddress(
        customerId: UUID,
        addressId: Long,
    ): AddressDO

}
