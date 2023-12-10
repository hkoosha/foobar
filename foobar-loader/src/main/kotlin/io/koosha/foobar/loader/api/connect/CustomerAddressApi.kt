package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "CustomerAddress",
    url = "\${foobar.url.customer}:\${foobar.port.customer}",
    path = "/foobar/customer/v1",
)
interface CustomerAddressApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/customer/{customerId}/address"]
    )
    fun create(
        @PathVariable("customerId")
        customerId: UUID,

        @RequestBody
        body: CreateDto,
    )

    data class CreateDto(
        val name: String,
        val city: String,
        val country: String,
        val zipcode: String,
        val addressLine1: String,
    )

}
