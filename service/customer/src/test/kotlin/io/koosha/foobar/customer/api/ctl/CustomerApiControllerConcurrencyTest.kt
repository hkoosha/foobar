package io.koosha.foobar.customer.api.ctl

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PROFILE__TEST
import io.koosha.foobar.customer.api.customer0
import io.koosha.foobar.customer.api.customer1
import io.koosha.foobar.customer.api.customer2
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.model.dto.CustomerCreateRequestDto
import io.koosha.foobar.customer.api.model.dto.CustomerCreateRequestNameDto
import io.koosha.foobar.customer.api.model.dto.CustomerUpdateRequestDto
import io.koosha.foobar.customer.api.model.dto.CustomerUpdateRequestNameDto
import io.koosha.foobar.customer.api.model.repo.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(PROFILE__TEST)
@Disabled
internal class CustomerApiControllerConcurrencyTest {

    @MockBean
    lateinit var repo: CustomerRepository

    @Autowired
    lateinit var om: ObjectMapper

    @Autowired
    lateinit var mvc: MockMvc

    @Value("\${foobar.rest.on-concurrency-error-retry-after}")
    var retryAfter: Duration? = null

    fun uri(customerId: Any? = null): String {
        val uriBuilder =
            if (customerId == null)
                UriComponentsBuilder
                    .fromUriString("/foobar/customer/v1/customer")
                    .buildAndExpand()
            else
                UriComponentsBuilder
                    .fromUriString("/foobar/customer/v1/customer/{customerId}")
                    .buildAndExpand(
                        mutableMapOf(
                            "customerId" to customerId.toString(),
                        )
                    )

        val uri = uriBuilder.toUri().toString()
        return uri
    }


    @BeforeEach
    fun setup() {

        reset(this.repo)
        clearInvocations(this.repo)

        `when`(this.repo.findById(customer0().customerId!!))
            .thenThrow(ConcurrencyFailureException("dummy00"))
        `when`(this.repo.findById(customer1().customerId!!))
            .thenThrow(ConcurrencyFailureException("dummy01"))
        `when`(this.repo.findById(customer2().customerId!!))
            .thenThrow(ConcurrencyFailureException("dummy02"))
        `when`(this.repo.findAll())
            .thenThrow(ConcurrencyFailureException("dummy03"))
        `when`(this.repo.save(any()))
            .thenThrow(ConcurrencyFailureException("dummy04"))

        assertThat(this.retryAfter).isNotNull
    }

    @Test
    fun `test getCustomers`() {

        val retryAfter = this.mvc
            .perform(get((uri())))
            .andExpect(status().`is`(HttpStatus.SERVICE_UNAVAILABLE.value()))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER))
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.RETRY_AFTER).toString()

        assertThat(retryAfter)
            .isEqualTo(this.retryAfter!!.toMillis().toString())
    }

    @Test
    fun `test getCustomer`() {

        val uri = this.uri(customer0().customerId)
        val retryAfter = this.mvc
            .perform(get(uri))
            .andExpect(status().`is`(HttpStatus.SERVICE_UNAVAILABLE.value()))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER))
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.RETRY_AFTER).toString()

        assertThat(retryAfter)
            .isEqualTo(this.retryAfter!!.toMillis().toString())
    }

    @Test
    fun `test patchCustomer`() {

        val dto = CustomerUpdateRequestDto(
            name = CustomerUpdateRequestNameDto(
                title = Title.MS,
                firstName = "foo0",
                lastName = "bar0",
            )
        )
        val requestContent = this.om.writeValueAsString(dto)
        val uri = this.uri(customer0().customerId)

        val retryAfter = this.mvc
            .perform(
                patch(uri)
                    .content(requestContent)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().`is`(HttpStatus.SERVICE_UNAVAILABLE.value()))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER))
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.RETRY_AFTER).toString()

        assertThat(retryAfter)
            .isEqualTo(this.retryAfter!!.toMillis().toString())
    }

    @Test
    fun `test postCustomer`() {

        val dto = CustomerCreateRequestDto(
            name = CustomerCreateRequestNameDto(
                title = Title.MS,
                firstName = "foo0",
                lastName = "bar0",
            )
        )
        val requestContent = this.om.writeValueAsString(dto)

        val retryAfter = this.mvc
            .perform(
                MockMvcRequestBuilders.post(uri())
                    .content(requestContent)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().`is`(HttpStatus.SERVICE_UNAVAILABLE.value()))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER))
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.RETRY_AFTER).toString()

        assertThat(retryAfter)
            .isEqualTo(this.retryAfter!!.toMillis().toString())
    }

    @Test
    fun `test deleteCustomer`() {

        val uri = uri(customer1().customerId)
        val retryAfter = this.mvc
            .perform(
                delete(uri)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().`is`(HttpStatus.SERVICE_UNAVAILABLE.value()))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER))
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.RETRY_AFTER).toString()

        assertThat(retryAfter)
            .isEqualTo(this.retryAfter!!.toMillis().toString())
    }

}
