package io.koosha.foobar.customer.api.service

import io.koosha.foobar.common.DefaultRandomUUIDProvider
import io.koosha.foobar.common.PROFILE__TEST
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.customer.api.IDS
import io.koosha.foobar.customer.api.addressDO0
import io.koosha.foobar.customer.api.addressDO1
import io.koosha.foobar.customer.api.addressReq0
import io.koosha.foobar.customer.api.customer0
import io.koosha.foobar.customer.api.customer1
import io.koosha.foobar.customer.api.customer2
import io.koosha.foobar.customer.api.model.AddressDO
import io.koosha.foobar.customer.api.model.AddressRepository
import io.koosha.foobar.customer.api.model.CustomerDO
import io.koosha.foobar.customer.api.model.CustomerRepository
import io.koosha.foobar.customer.api.model.CustomerState
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.strOfLen
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowableOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.validation.Validator

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
)
@ActiveProfiles(PROFILE__TEST)
class CustomerServiceImplTest {

    lateinit var subject: CustomerServiceImpl

    @Autowired
    lateinit var validator: Validator

    @MockBean
    lateinit var customerRepo: CustomerRepository

    @MockBean
    lateinit var addressRepo: AddressRepository

    @BeforeEach
    fun setup() {

        assertThat(IDS)
            .hasSize(3)

        reset(this.customerRepo)
        reset(this.addressRepo)

        this.subject = CustomerServiceImpl(
            this.customerRepo,
            this.addressRepo,
            this.validator,
            DefaultRandomUUIDProvider()
        )

        `when`(customerRepo.findById(customer0().customerId!!))
            .thenReturn(Optional.of(customer0()))
        `when`(customerRepo.findById(customer1().customerId!!))
            .thenReturn(Optional.of(customer1()))
        `when`(customerRepo.findById(customer2().customerId!!))
            .thenReturn(Optional.empty())
        `when`(customerRepo.findAll())
            .thenReturn(listOf(customer0(), customer1()))

        clearInvocations(this.customerRepo)
        clearInvocations(this.addressRepo)
    }

    private inline fun <reified T> any(): T = any(T::class.java)

    @Nested
    inner class Find {

        @Test
        fun `test findById returns empty Optional for non existing customer`() {

            val findById = subject.findById(customer2().customerId!!)
            assertThat(findById).isEmpty
        }

        @Test
        fun `test findById`() {

            val found0 = subject.findById(customer0().customerId!!).get()
            assertThat(found0).isEqualTo(customer0())
        }

        @Test
        fun `test findAll`() {

            val all = subject.findAll()
            assertThat(all).containsOnly(customer0(), customer1())
        }

    }

    @Nested
    inner class Create {

        @Test
        fun `test create`() {

            `when`(customerRepo.save(any()))
                .thenReturn(customer0())

            val req = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = Title.NOT_SPECIFIED,
                    firstName = "foo",
                    lastName = "bar"
                )
            )

            subject.create(req)

            verify(customerRepo, times(1)).save(any())

            // TODO capture customerRepo.save()'s arg and compare fields.
        }

        @Test
        fun `test create0`() {

            val req = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = Title.NOT_SPECIFIED,
                    firstName = "foo",
                    lastName = "bar"
                )
            )

            val created = subject.create0(req)

            assertThat(created.customerId).isNotNull
            assertThat(created.state).isEqualTo(CustomerState.ACTIVE)
            assertThat(created.name.title).isEqualTo(req.name?.title)
            assertThat(created.name.firstName).isEqualTo(req.name?.firstName)
            assertThat(created.name.lastName).isEqualTo(req.name?.lastName)
            assertThat(created.addressIdPool).isEqualTo(0L)
        }

        @Test
        fun `test create with empty values throws exception`() {

            val req = CustomerCreateRequestName(
                title = Title.NOT_SPECIFIED,
                firstName = "",
                lastName = "",
            )

            val ex = catchThrowableOfType(
                {
                    subject.create(CustomerCreateRequest(name = req))
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into errors
        }

        @Test
        fun `test create with null values throws exception`() {

            val req = CustomerCreateRequestName(
                title = null,
                firstName = null,
                lastName = null,
            )

            val ex = catchThrowableOfType(
                {
                    subject.create(CustomerCreateRequest(name = req))
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into errors
        }

        @Test
        fun `test create with too long values throws exception`() {

            val req = CustomerCreateRequestName(
                title = Title.NOT_SPECIFIED,
                firstName = strOfLen(FIRST_NAME_MAX_LEN + 1),
                lastName = strOfLen(LAST_NAME_MAX_LEN + 1),
            )

            val ex = catchThrowableOfType(
                {
                    subject.create(CustomerCreateRequest(name = req))
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into errors
        }

    }

    @Nested
    inner class Update {

        @Test
        fun `test update values`() {

            // TODO

        }

        @Test
        fun `test update empty values throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.update(
                        customer0().customerId!!,
                        CustomerUpdateRequest(
                            CustomerUpdateRequestName(
                                title = null,
                                firstName = "",
                                lastName = "",
                            ),
                        ),
                    )
                },
                EntityBadValueException::class.java
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())

            assertThat(ex).isNotNull

            // TODO look into errors.

        }

        @Test
        fun `test update too long values throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.update(
                        customer0().customerId!!,
                        CustomerUpdateRequest(
                            CustomerUpdateRequestName(
                                title = null,
                                firstName = strOfLen(FIRST_NAME_MAX_LEN + 1),
                                lastName = strOfLen(LAST_NAME_MAX_LEN + 1),
                            ),
                        ),
                    )
                },
                EntityBadValueException::class.java
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())

            assertThat(ex).isNotNull

            // TODO look into errors.
        }

        @Test
        fun `test update on inactive customer throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.update(
                        customer1().customerId!!,
                        CustomerUpdateRequest(
                            CustomerUpdateRequestName(
                                Title.MS,
                                firstName = "foo",
                                lastName = "bar",
                            ),
                        ),
                    )
                },
                EntityInIllegalStateException::class.java
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())

            assertThat(ex).isNotNull
        }

        @Test
        fun `test update does not touch entity when there is nothing to update`() {

            subject.update(
                customer0().customerId!!,
                CustomerUpdateRequest(
                    CustomerUpdateRequestName(
                        title = null,
                        firstName = null,
                        lastName = null,
                    ),
                ),
            )

            subject.update(
                customer0().customerId!!,
                CustomerUpdateRequest(name = null),
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())
        }

    }

    @Nested
    inner class Delete {

        @Test
        fun `test delete`() {

            doNothing().`when`(customerRepo).delete(any())
            doNothing().`when`(addressRepo).delete(any())

            subject.delete(customer1().customerId!!)

            verify(customerRepo, times(1))
                .delete(customer1())
        }

        @Test
        fun `test delete ignores non existing entities`() {

            subject.delete(customer2().customerId!!)
            verify(customerRepo, never()).delete(any())
        }

        @Test
        fun `test delete does not allow deleting active customers`() {

            doNothing().`when`(customerRepo).delete(any())
            doNothing().`when`(addressRepo).delete(any())

            val ex = catchThrowableOfType(
                {
                    subject.delete(customer0().customerId!!)
                },
                EntityInIllegalStateException::class.java,
            )

            verify(customerRepo, never()).delete(any())
            verify(addressRepo, never()).delete(any())

            assertThat(ex)
                .isNotNull
                .hasMessage("deletion not allowed in current state")

            assertThat(ex.context)
                .containsOnly(
                    EntityInfo(
                        entityType = CustomerDO.ENTITY_TYPE,
                        entityId = customer0().customerId,
                    )
                )
        }

    }

    @Nested
    inner class AddressAdd {

        @Test
        fun `test addAddress`() {

            `when`(addressRepo.save(any()))
                .thenReturn(addressDO0())

            subject.addAddress(customer0().customerId!!, addressReq0())

            verify(addressRepo, times(1)).save(any())

            // TODO instead of mocking, capture addressRepo.save()'s arg and compare fields.
        }

        @Test
        fun `test addAddress address null values throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.addAddress(
                        customer0().customerId!!,
                        CustomerAddressCreateRequest(
                            zipcode = null,
                            country = null,
                            city = null,
                            addressLine1 = null,
                            name = null,
                        )
                    )
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test addAddress address long values throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.addAddress(
                        customer0().customerId!!,
                        CustomerAddressCreateRequest(
                            zipcode = strOfLen(ADDRESS_ZIPCODE_MAX_LEN + 1),
                            country = strOfLen(ADDRESS_COUNTRY_MAX_LEN + 1),
                            city = strOfLen(ADDRESS_CITY_MAX_LEN + 1),
                            addressLine1 = strOfLen(ADDRESS_ADDRESS_LINE1_MAX_LEN + 1),
                            name = strOfLen(ADDRESS_NAME_MAX_LEN + 1),
                        )
                    )
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test addAddress address empty values throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.addAddress(
                        customer0().customerId!!,
                        CustomerAddressCreateRequest(
                            zipcode = "",
                            country = "",
                            city = "",
                            addressLine1 = "",
                            name = "",
                        )
                    )
                },
                EntityBadValueException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test addAddress does not touch customers which are not in active state`() {

            val ex = catchThrowableOfType(
                {
                    subject.addAddress(customer1().customerId!!, addressReq0())
                },
                EntityInIllegalStateException::class.java
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test addAddress on non existing customer throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.addAddress(customer2().customerId!!, addressReq0())
                },
                EntityNotFoundException::class.java
            )

            verify(customerRepo, never()).save(any())
            verify(addressRepo, never()).save(any())

            assertThat(ex).isNotNull

            // TODO look into ex
        }

    }

    @Nested
    inner class AddressDelete {

        @Test
        fun `test deleteAddress`() {

            `when`(
                addressRepo.findByAddressPk(
                    AddressDO.Pk(
                        addressId = 3,
                        customer = customer0(),
                    )
                )
            ).thenReturn(Optional.of(addressDO0()))

            subject.deleteAddress(customer0().customerId!!, 3)
            verify(addressRepo, times(1)).delete(addressDO0())
        }

        @Test
        fun `test deleteCustomer deletes addresses too`() {

            subject.delete(customer1().customerId!!)
            verify(addressRepo, times(1)).deleteByAddressPk_Customer_customerId(customer1().customerId!!)
        }

        @Test
        fun `test deleteAddress on non existing customer throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.deleteAddress(customer2().customerId!!, 42L)
                },
                EntityNotFoundException::class.java
            )

            verify(customerRepo, never()).delete(any())
            verify(addressRepo, never()).delete(any())

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test deleteAddress ignores non existing addresses`() {

            `when`(addressRepo.findByAddressPk(any()))
                .thenReturn(Optional.empty())

            subject.deleteAddress(customer0().customerId!!, 42L)

            verify(customerRepo, never()).delete(any())
            verify(addressRepo, never()).delete(any())
        }

        @Test
        fun `test deleteAddress on inactive customers throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.deleteAddress(customer1().customerId!!, 42L)
                },
                EntityInIllegalStateException::class.java,
            )

            assertThat(ex).isNotNull

            verify(customerRepo, never()).delete(any())
            verify(addressRepo, never()).delete(any())

            // TODO look into ex
        }

    }

    @Nested
    inner class AddressGet {

        @Test
        fun `test getAddresses`() {

            `when`(addressRepo.findByAddressPk_Customer(customer0()))
                .thenReturn(listOf(addressDO0(), addressDO1()))

            val find = subject.getAddressesOfCustomer(customer0().customerId!!)

            assertThat(find)
                .containsExactlyInAnyOrder(addressDO0(), addressDO1())
        }

        @Test
        fun `test getAddress`() {

            `when`(addressRepo.findByAddressPk_Customer_customerIdAndAddressPk_addressId(customer0().customerId!!, 3))
                .thenReturn(Optional.of(addressDO0()))

            val get = subject.getAddress(customer0().customerId!!, 3)

            assertThat(get)
                .isEqualTo(addressDO0())
        }

        @Test
        fun `test getAddress on non existing customer throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.getAddress(customer2().customerId!!, 42L)
                },
                EntityNotFoundException::class.java
            )

            verifyNoInteractions(addressRepo)

            assertThat(ex).isNotNull

            // TODO look into ex
        }

        @Test
        fun `test getAddress on non existing address throws exception`() {

            val ex = catchThrowableOfType(
                {
                    subject.getAddress(customer0().customerId!!, 42L)
                },
                EntityNotFoundException::class.java
            )

            assertThat(ex).isNotNull

            // TODO look into ex
        }

    }

}
