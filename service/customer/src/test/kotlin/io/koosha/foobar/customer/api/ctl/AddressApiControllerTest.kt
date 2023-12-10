package io.koosha.foobar.customer.api.ctl

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PROFILE__TEST
import io.koosha.foobar.common.model.EntityBadValueApiError
import io.koosha.foobar.common.model.EntityNotFoundApiError
import io.koosha.foobar.customer.api.IDS
import io.koosha.foobar.customer.api.NOW
import io.koosha.foobar.customer.api.addressReq0
import io.koosha.foobar.customer.api.addressReq1
import io.koosha.foobar.customer.api.customer1
import io.koosha.foobar.customer.api.customer2
import io.koosha.foobar.customer.api.model.ADDRESS_ADDRESS_LINE1_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_CITY_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_COUNTRY_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_NAME_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_ZIPCODE_MAX_LEN
import io.koosha.foobar.customer.api.model.dto.CustomerAddressCreateRequestDto
import io.koosha.foobar.customer.api.model.entity.AddressDO
import io.koosha.foobar.customer.api.model.entity.CustomerDO
import io.koosha.foobar.customer.api.model.repo.AddressRepository
import io.koosha.foobar.customer.api.model.repo.CustomerRepository
import io.koosha.foobar.customer.api.strOfLen
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.util.UriComponentsBuilder
import java.util.Optional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(PROFILE__TEST)
class AddressApiControllerTest {

    @MockBean
    lateinit var customerRepo: CustomerRepository

    @MockBean
    lateinit var addressRepo: AddressRepository

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var om: ObjectMapper


    private val postCustomerAddressRequest = CustomerAddressCreateRequestDto(
        name = addressDO0().name,
        zipcode = addressDO0().zipcode,
        addressLine1 = addressDO0().addressLine1,
        country = addressDO0().country,
        city = addressDO0().city,
    )

    private fun customer0(): CustomerDO = io.koosha.foobar.customer.api.customer0().also {
        it.addressIdPool = 2
    }

    private fun addressDO0() = AddressDO(
        AddressDO.AddressPk(
            addressId = 0,
            customer = this.customer0(),
        ),
        version = null,
        created = NOW,
        updated = NOW,
        name = addressReq0().name,
        zipcode = addressReq0().zipcode,
        addressLine1 = addressReq0().addressLine1,
        country = addressReq0().country,
        city = addressReq0().city,
    )

    private fun addressDO1() = AddressDO(
        AddressDO.AddressPk(
            addressId = 1,
            customer = this.customer0(),
        ),
        version = null,
        created = NOW,
        updated = NOW,
        name = addressReq1().name,
        zipcode = addressReq1().zipcode,
        addressLine1 = addressReq1().addressLine1,
        country = addressReq1().country,
        city = addressReq1().city,
    )

    fun uri(
        customerId: Any,
        addressId: Any? = null,
    ): String {

        val uriBuilder =
            if (addressId == null) {
                UriComponentsBuilder
                    .fromUriString("/foobar/customer/v1/customer/{customerId}/address")
                    .buildAndExpand(
                        mutableMapOf(
                            "customerId" to customerId.toString(),
                        )
                    )
            }
            else {
                UriComponentsBuilder
                    .fromUriString("/foobar/customer/v1/customer/{customerId}/address/{addressId}")
                    .buildAndExpand(
                        mutableMapOf(
                            "customerId" to customerId.toString(),
                            "addressId" to addressId.toString(),
                        )
                    )
            }

        val uri = uriBuilder.toUri().toString()
        return uri
    }


    @BeforeEach
    fun setup() {

        assertThat(IDS).hasSize(3)

        `when`(this.customerRepo.findById(customer0().customerId!!))
            .thenReturn(Optional.of(customer0()))

        `when`(this.customerRepo.findById(customer1().customerId!!))
            .thenReturn(Optional.of(customer1()))

        `when`(this.addressRepo.findByAddressPk_Customer(customer0()))
            .thenReturn(listOf(addressDO0(), addressDO1()))

        `when`(
            this.addressRepo.findByAddressPk_Customer_customerIdAndAddressPk_addressId(
                customer0().customerId!!,
                addressDO1().addressPk.addressId!!
            )
        )
            .thenReturn(Optional.of(addressDO1()))

        `when`(this.addressRepo.findByAddressPk(addressDO0().addressPk))
            .thenReturn(Optional.of(addressDO0()))
    }

    @Test
    fun `test addressDOMapper`() {

        val entity = AddressDO(
            addressPk = AddressDO.AddressPk(
                addressId = 42L,
                customer = this.customer0(),
            ),
            version = 0L,
            created = NOW,
            updated = NOW,
            name = "some where",
            zipcode = "12340",
            addressLine1 = "some place",
            country = "Germany",
            city = "Frankfurt",
        )

        val dto = AddressApiController.Address(entity)

        assertThat(dto.addressId).isEqualTo(entity.addressPk.addressId)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.zipcode).isEqualTo(entity.zipcode)
        assertThat(dto.addressLine1).isEqualTo(entity.addressLine1)
        assertThat(dto.country).isEqualTo(entity.country)
        assertThat(dto.city).isEqualTo(entity.city)
    }

    @Nested
    open inner class Get {

        @Test
        fun `test getAllCustomerAddresses`() {

            val uri = uri(customer0().customerId!!)

            mvc.perform(get(uri))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andExpect(
                    jsonPath(
                        "$..addressId",
                        hasItems(
                            equalTo(0),
                            equalTo(1),
                        )
                    )
                )
        }

        @Test
        fun `test getAllCustomerAddresses of non existing customer fails`() {

            val uri = uri(customer2().customerId!!)

            mvc
                .perform(get(uri))
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message", equalTo("entity or entities not found")))
                .andExpect(jsonPath("$.error", equalTo(EntityNotFoundApiError.ERROR)))
                .andExpect(jsonPath("$.context", hasSize<List<*>>(1)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo("customer")))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer2().customerId.toString())))
        }

        @Test
        open fun `test getAllCustomerAddresses with no address`() {

            val uri = uri(customer1().customerId!!)

            mvc
                .perform(get(uri))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.length()", equalTo(0)))
        }

        @Test
        fun `test getCustomerAddress`() {

            val uri = uri(customer0().customerId!!, addressDO1().addressPk.addressId)
            mvc.perform(get(uri))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.addressId", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(addressDO1().name)))
                .andExpect(jsonPath("$.zipcode", equalTo(addressDO1().zipcode)))
                .andExpect(jsonPath("$.addressLine1", equalTo(addressDO1().addressLine1)))
                .andExpect(jsonPath("$.country", equalTo(addressDO1().country)))
                .andExpect(jsonPath("$.city", equalTo(addressDO1().city)))
        }

        @Test
        fun `test getCustomerAddress of non existing address fails`() {

            val uri0 = uri(customer0().customerId!!, 42)
            mvc.perform(get(uri0))
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message", equalTo("entity or entities not found")))
                .andExpect(jsonPath("$.error", equalTo(EntityNotFoundApiError.ERROR)))
                .andExpect(jsonPath("$.context", hasSize<List<*>>(2)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo("customer")))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer0().customerId.toString())))
                .andExpect(jsonPath("$.context[1].entityType", equalTo("address")))
                .andExpect(jsonPath("$.context[1].entityId", equalTo(42)))
        }
    }

    @Nested
    open inner class Post {

        @Test
        open fun `test postCustomerAddress`() {

            val content = om.writeValueAsString(postCustomerAddressRequest)
            val uri = uri(customer0().customerId!!)
            val expectedId = (customer0().addressIdPool!! + 1).toInt()
            val uriRedirect = uri(customer0().customerId!!, expectedId)


            `when`(addressRepo.save(any()))
                .thenReturn(
                    AddressDO(
                        addressPk = AddressDO.AddressPk(
                            addressId = expectedId.toLong(),
                            customer = customer0(),
                        ),
                        name = postCustomerAddressRequest.name,
                        zipcode = postCustomerAddressRequest.zipcode,
                        addressLine1 = postCustomerAddressRequest.addressLine1,
                        country = postCustomerAddressRequest.country,
                        city = postCustomerAddressRequest.city,
                    )
                )

            mvc
                .perform(
                    post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().`is`(HttpStatus.CREATED.value()))
                .andExpect(redirectedUrlPattern("http*://*/$uriRedirect"))
                .andExpect(jsonPath("$.addressId", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(postCustomerAddressRequest.name)))
                .andExpect(jsonPath("$.zipcode", equalTo(postCustomerAddressRequest.zipcode)))
                .andExpect(jsonPath("$.addressLine1", equalTo(postCustomerAddressRequest.addressLine1)))
                .andExpect(jsonPath("$.country", equalTo(postCustomerAddressRequest.country)))
                .andExpect(jsonPath("$.city", equalTo(postCustomerAddressRequest.city)))
        }

        @Test
        open fun `test postCustomerAddress rejects null values`() {

            val req = CustomerAddressCreateRequestDto(
                name = null,
                zipcode = null,
                addressLine1 = null,
                country = null,
                city = null,
            )

            val content = om.writeValueAsString(req)
            val uri = uri(customer0().customerId!!)

            mvc
                .perform(
                    post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", equalTo("bad value for entity")))
                .andExpect(jsonPath("$.error", equalTo(EntityBadValueApiError.ERROR)))
                .andExpect(jsonPath("$.context.zipcode", hasItems("must not be null")))
                .andExpect(jsonPath("$.context.country", hasItems("must not be null")))
                .andExpect(jsonPath("$.context.city", hasItems("must not be null")))
                .andExpect(jsonPath("$.context.name", hasItems("must not be null")))
                .andExpect(jsonPath("$.context.addressLine1", hasItems("must not be null")))
        }

        @Test
        open fun `test postCustomerAddress rejects empty values`() {

            val req = CustomerAddressCreateRequestDto(
                name = "",
                zipcode = "",
                addressLine1 = "",
                country = "",
                city = "",
            )
            val content = om.writeValueAsString(req)
            val uri = uri(customer0().customerId!!)

            mvc
                .perform(
                    post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", equalTo("bad value for entity")))
                .andExpect(jsonPath("$.error", equalTo(EntityBadValueApiError.ERROR)))
                .andExpect(
                    jsonPath(
                        "$.context.zipcode",
                        hasItems("size must be between 1 and $ADDRESS_ZIPCODE_MAX_LEN")
                    )
                )
                .andExpect(
                    jsonPath(
                        "$.context.country",
                        hasItems("size must be between 1 and $ADDRESS_COUNTRY_MAX_LEN")
                    )
                )
                .andExpect(jsonPath("$.context.city", hasItems("size must be between 1 and $ADDRESS_CITY_MAX_LEN")))
                .andExpect(jsonPath("$.context.name", hasItems("size must be between 1 and $ADDRESS_NAME_MAX_LEN")))
                .andExpect(
                    jsonPath(
                        "$.context.addressLine1",
                        hasItems("size must be between 1 and $ADDRESS_ADDRESS_LINE1_MAX_LEN")
                    )
                )
        }

        @Test
        open fun `test postCustomerAddress rejects too long values`() {

            val req = CustomerAddressCreateRequestDto(
                name = strOfLen(ADDRESS_NAME_MAX_LEN + 1),
                zipcode = strOfLen(ADDRESS_ZIPCODE_MAX_LEN + 1),
                addressLine1 = strOfLen(ADDRESS_ADDRESS_LINE1_MAX_LEN + 1),
                country = strOfLen(ADDRESS_COUNTRY_MAX_LEN + 1),
                city = strOfLen(ADDRESS_CITY_MAX_LEN + 1),
            )
            val content = om.writeValueAsString(req)
            val uri = uri(customer0().customerId!!)

            mvc
                .perform(
                    post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", equalTo("bad value for entity")))
                .andExpect(jsonPath("$.error", equalTo(EntityBadValueApiError.ERROR)))
                .andExpect(
                    jsonPath(
                        "$.context.zipcode",
                        hasItems("size must be between 1 and $ADDRESS_ZIPCODE_MAX_LEN")
                    )
                )
                .andExpect(
                    jsonPath(
                        "$.context.country",
                        hasItems("size must be between 1 and $ADDRESS_COUNTRY_MAX_LEN")
                    )
                )
                .andExpect(jsonPath("$.context.city", hasItems("size must be between 1 and $ADDRESS_CITY_MAX_LEN")))
                .andExpect(jsonPath("$.context.name", hasItems("size must be between 1 and $ADDRESS_NAME_MAX_LEN")))
                .andExpect(
                    jsonPath(
                        "$.context.addressLine1",
                        hasItems("size must be between 1 and $ADDRESS_ADDRESS_LINE1_MAX_LEN")
                    )
                )
        }

        @Test
        fun `test postCustomerAddress on inactive customer says http error`() {

            val content = om.writeValueAsString(postCustomerAddressRequest)
            val uri = uri(customer1().customerId!!)

            mvc
                .perform(
                    post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.context.length()", equalTo(1)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo("customer")))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer1().customerId.toString())))
                .andExpect(jsonPath("$.message", equalTo("operation not allowed with current state of entity")))
        }
    }

    @Nested
    open inner class Delete {

        @Test
        open fun `test deleteCustomerAddress`() {

            val uri = uri(customer0().customerId!!, addressDO0().addressPk.addressId)

            mvc
                .perform(delete(uri))
                .andExpect(status().`is`(HttpStatus.NO_CONTENT.value()))

            verify(addressRepo, times(1)).delete(addressDO0())
        }

        @Test
        fun `test deleteCustomerAddress ignores non existing address`() {

            val uri = uri(customer0().customerId!!, 42)

            mvc
                .perform(delete(uri))
                .andExpect(status().`is`(HttpStatus.NO_CONTENT.value()))
        }

        @Test
        fun `test deleteCustomerAddress on non existing customer says http error`() {

            val uri = uri(customer2().customerId!!, 0)

            mvc
                .perform(delete(uri))
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error", equalTo(EntityNotFoundApiError.ERROR)))
                .andExpect(jsonPath("$.message", equalTo("entity or entities not found")))
                .andExpect(jsonPath("$.context.length()", equalTo(1)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo("customer")))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer2().customerId.toString())))

        }

        @Test
        fun `test deleteCustomerAddress on inactive customer says http error`() {

            val uri = uri(customer1().customerId!!, 0)

            mvc
                .perform(delete(uri))
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.context[0].entityType", equalTo("customer")))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer1().customerId.toString())))
                .andExpect(jsonPath("$.message", equalTo("operation not allowed with current state of entity")))
        }

    }

}
