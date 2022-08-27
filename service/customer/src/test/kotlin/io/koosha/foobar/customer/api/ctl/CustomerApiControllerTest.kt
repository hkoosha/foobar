package io.koosha.foobar.customer.api.ctl

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PROFILE__TEST
import io.koosha.foobar.common.model.EntityBadValueApiError
import io.koosha.foobar.customer.API_PATH_PREFIX
import io.koosha.foobar.customer.api.IDS
import io.koosha.foobar.customer.api.customer0
import io.koosha.foobar.customer.api.customer1
import io.koosha.foobar.customer.api.customer2
import io.koosha.foobar.customer.api.model.AddressRepository
import io.koosha.foobar.customer.api.model.CustomerDO
import io.koosha.foobar.customer.api.model.CustomerRepository
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.service.CustomerCreateRequest
import io.koosha.foobar.customer.api.service.CustomerCreateRequestName
import io.koosha.foobar.customer.api.service.CustomerService
import io.koosha.foobar.customer.api.service.CustomerUpdateRequest
import io.koosha.foobar.customer.api.service.CustomerUpdateRequestName
import io.koosha.foobar.customer.api.service.FIRST_NAME_MAX_LEN
import io.koosha.foobar.customer.api.service.LAST_NAME_MAX_LEN
import io.koosha.foobar.customer.api.strOfLen
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.util.UriComponentsBuilder
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(PROFILE__TEST)
internal class CustomerApiControllerTest {

    @MockBean
    lateinit var customerRepo: CustomerRepository

    @MockBean
    lateinit var addressRepository: AddressRepository

    @SpyBean
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var om: ObjectMapper

    val updateRequest = CustomerUpdateRequest(
        name = CustomerUpdateRequestName(
            title = Title.MS,
            firstName = "foo0",
            lastName = "bar0",
        )
    )

    fun uri(customerId: Any? = null): String {
        val uriBuilder =
            if (customerId == null)
                UriComponentsBuilder
                    .fromUriString(CustomerApiController.URI)
                    .buildAndExpand()
            else
                UriComponentsBuilder
                    .fromUriString("${CustomerApiController.URI}/{${CustomerApiController.URI__PART__CUSTOMER_ID}}")
                    .buildAndExpand(
                        mutableMapOf(
                            CustomerApiController.URI__PART__CUSTOMER_ID to customerId.toString(),
                        )
                    )

        val uri = uriBuilder.toUri().toString()
        return uri
    }


    @BeforeEach
    fun setup() {

        assertThat(IDS).hasSize(3)

        reset(customerRepo)
        reset(addressRepository)
        clearInvocations(customerRepo)
        clearInvocations(addressRepository)

        `when`(customerRepo.findAll())
            .thenReturn(listOf(customer0(), customer1()))

        `when`(customerRepo.findById(customer0().customerId!!))
            .thenReturn(Optional.of(customer0()))

        `when`(customerRepo.findById(customer1().customerId!!))
            .thenReturn(Optional.of(customer1()))
    }

    @Test
    fun `test customerDOMapper`() {

        val dto = CustomerApiController.Customer(customer0())

        assertThat(dto.customerId).isEqualTo(customer0().customerId)
        assertThat(dto.name.title).isEqualTo(customer0().name.title)
        assertThat(dto.name.firstName).isEqualTo(customer0().name.firstName)
        assertThat(dto.name.lastName).isEqualTo(customer0().name.lastName)
        assertThat(dto.isActive).isTrue
    }

    @Test
    fun `test customerDOMapper on inactive customer`() {

        val dto = CustomerApiController.Customer(customer1())
        assertThat(dto.isActive).isFalse
    }

    @Nested
    inner class Get {

        @Test
        fun `test getAllCustomers`() {

            mvc
                .perform(get(uri()))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andExpect(
                    jsonPath(
                        "$..customerId", hasItems(
                            equalTo(customer0().customerId.toString()),
                            equalTo(customer1().customerId.toString()),
                        )
                    )
                )
        }

        @Test
        fun `test getAllCustomers with no customer`() {

            clearInvocations(customerRepo)
            reset(customerRepo)

            mvc
                .perform(get(uri()))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.length()", equalTo(0)))
        }

        @Test
        fun `test getCustomer on non existing customer says http not found`() {

            val uri = uri(customer2().customerId!!)
            mvc.perform(get(uri))
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
        }

        @Test
        fun `test getCustomer`() {

            val uri = uri(customer1().customerId!!)
            mvc
                .perform(get(uri))
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.customerId", equalTo(customer1().customerId.toString())))
                .andExpect(jsonPath("$.name.title", equalTo(customer1().name.title.toString())))
                .andExpect(jsonPath("$.name.firstName", equalTo(customer1().name.firstName.toString())))
                .andExpect(jsonPath("$.name.lastName", equalTo(customer1().name.lastName.toString())))
                .andExpect(jsonPath("$.isActive", equalTo(false)))
        }

    }

    @Nested
    inner class Patch {

        @Test
        fun `test patchCustomer`() {

            val updated = customer0()
            updated.name.title = Title.MS
            updated.name.firstName = "foo0"
            updated.name.lastName = "bar0"
            val requestContent = om.writeValueAsString(updateRequest)
            val uri = uri(customer0().customerId)

            `when`(customerRepo.save(any()))
                .thenReturn(updated)

            mvc
                .perform(
                    patch(uri)
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.customerId", equalTo(customer0().customerId.toString())))
                .andExpect(jsonPath("$.name.title", equalTo(Title.MS.toString())))
                .andExpect(jsonPath("$.name.firstName", equalTo("foo0")))
                .andExpect(jsonPath("$.name.lastName", equalTo("bar0")))

            verify(customerService, times(1))
                .update(customer0().customerId!!, updateRequest)
        }

        @Test
        fun `test patchCustomer on inactive customer says http error`() {

            val uri = uri(customer1().customerId)
            val requestContent = om.writeValueAsString(updateRequest)

            mvc
                .perform(
                    patch(uri)
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.context.length()", equalTo(1)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo(CustomerDO.ENTITY_TYPE)))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer1().customerId.toString())))
                .andExpect(jsonPath("$.message", equalTo(EntityErrorAdvice.ENTITY_ILLEGAL_STATE_MSG)))
        }

        @Test
        fun `test patchCustomer on non existing customer says http error`() {

            val uri = uri(customer2().customerId)
            val requestContent = om.writeValueAsString(updateRequest)

            mvc
                .perform(
                    patch(uri)
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.context.length()", equalTo(1)))
                .andExpect(jsonPath("$.context[0].entityType", equalTo(CustomerDO.ENTITY_TYPE)))
                .andExpect(jsonPath("$.context[0].entityId", equalTo(customer2().customerId.toString())))
                .andExpect(jsonPath("$.message", equalTo(EntityErrorAdvice.ENTITY_NOT_FOUND_MSG)))
        }

        @Test
        fun `test patchCustomer empty name firstName says http error`() {

            val uri = uri(customer0().customerId)
            val dto = CustomerUpdateRequest(
                name = CustomerUpdateRequestName(
                    title = null,
                    firstName = "",
                    lastName = "",
                )
            )
            val requestContent = om.writeValueAsString(dto)

            mvc
                .perform(
                    patch(uri)
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(
                    jsonPath(
                        "$.context.['name.firstName']",
                        hasItems("size must be between 1 and $FIRST_NAME_MAX_LEN")
                    )
                )
                .andExpect(
                    jsonPath(
                        "$.context.['name.lastName']",
                        hasItems("size must be between 1 and $LAST_NAME_MAX_LEN")
                    )
                )
        }

        @Test
        fun `test patchCustomer invalid name title says http error`() {

            // TODO get validation_error out of response

            val dto = CustomerUpdateRequest(
                name = CustomerUpdateRequestName(
                    title = Title.NOT_SPECIFIED,
                    firstName = "foo0",
                    lastName = "bar1",
                )
            )
            val requestContent = om
                .writeValueAsString(dto)
                .replace(Title.NOT_SPECIFIED.toString(), "dummy")

            mvc
                .perform(
                    patch("/$API_PATH_PREFIX/customers/${customer0().customerId}")
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
        }

    }

    @Nested
    inner class Post {

        @Test
        fun `test postCustomer`() {

            val saved = customer0()
            saved.customerId = UUID.fromString("30000000-0000-0000-0000-000000000000")
            val dto = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = Title.MS,
                    firstName = "foo0",
                    lastName = "bar0",
                )
            )
            val requestContent = om.writeValueAsString(dto)

            `when`(customerRepo.save(any()))
                .thenReturn(saved)

            mvc
                .perform(
                    post(uri())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.CREATED.value()))
                .andExpect(redirectedUrlPattern("http*://*/$API_PATH_PREFIX/customers/${saved.customerId}"))
                .andExpect(jsonPath("$.name.title", equalTo(saved.name.title.toString())))
                .andExpect(jsonPath("$.name.firstName", equalTo(saved.name.firstName)))
                .andExpect(jsonPath("$.name.lastName", equalTo(saved.name.lastName)))
                .andExpect(jsonPath("$.isActive", equalTo(true)))
        }

        @Test
        fun `test postCustomer null name says http error`() {

            val dto = CustomerCreateRequest(name = null)
            val requestContent = om.writeValueAsString(dto)

            mvc
                .perform(
                    post(uri())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error", equalTo(EntityBadValueApiError.ERROR)))
                .andExpect(jsonPath("$.message", equalTo(EntityErrorAdvice.ENTITY_BAD_VALUE_MSG)))
                .andExpect(jsonPath("$.context.name[0]", equalTo("must not be null")))
        }

        @Test
        fun `test postCustomer null name values says http error`() {

            val dto = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = null,
                    firstName = null,
                    lastName = null,
                )
            )
            val requestContent = om.writeValueAsString(dto)

            mvc
                .perform(
                    post(uri())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.context['name.title']", hasItems("must not be null")))
                .andExpect(jsonPath("$.context['name.firstName']", hasItems("must not be null")))
                .andExpect(jsonPath("$.context['name.lastName']", hasItems("must not be null")))
        }

        @Test
        fun `test postCustomer empty name values says http error`() {

            val dto = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = Title.NOT_SPECIFIED,
                    firstName = "",
                    lastName = "",
                )
            )
            val requestContent = om.writeValueAsString(dto)

            mvc
                .perform(
                    post(uri())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(
                    jsonPath(
                        "$.context['name.firstName']",
                        hasItems("size must be between 1 and $FIRST_NAME_MAX_LEN")
                    )
                )
                .andExpect(
                    jsonPath(
                        "$.context['name.lastName']",
                        hasItems("size must be between 1 and $LAST_NAME_MAX_LEN")
                    )
                )
        }

        @Test
        fun `test postCustomer too long name values says http error`() {

            val dto = CustomerCreateRequest(
                name = CustomerCreateRequestName(
                    title = Title.NOT_SPECIFIED,
                    firstName = strOfLen(FIRST_NAME_MAX_LEN + 1),
                    lastName = strOfLen(LAST_NAME_MAX_LEN + 1),
                )
            )
            val requestContent = om.writeValueAsString(dto)

            mvc
                .perform(
                    post(uri())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(
                    jsonPath(
                        "$.context['name.firstName']",
                        hasItems("size must be between 1 and $FIRST_NAME_MAX_LEN")
                    )
                )
                .andExpect(
                    jsonPath(
                        "$.context['name.lastName']",
                        hasItems("size must be between 1 and $LAST_NAME_MAX_LEN")
                    )
                )
        }

    }

    @Nested
    inner class Delete {

        @Test
        fun `test apiDelete`() {

            doNothing().`when`(customerRepo).delete(customer0())
            doNothing().`when`(addressRepository).deleteByAddressPk_Customer_customerId(customer0().customerId!!)

            val uri = uri(customer1().customerId!!)

            mvc
                .perform(
                    delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.NO_CONTENT.value()))

            verify(customerService, times(1))
                .delete(customer1().customerId!!)
        }

        @Test
        fun `test apiDelete on non existing customer has no effect`() {

            val uri = uri(customer2().customerId!!)

            mvc
                .perform(
                    delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.NO_CONTENT.value()))
        }

        @Test
        fun `test apiDelete on active customer is disallowed`() {

            val uri = uri(customer0().customerId!!)

            mvc
                .perform(
                    delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
        }

    }

}
