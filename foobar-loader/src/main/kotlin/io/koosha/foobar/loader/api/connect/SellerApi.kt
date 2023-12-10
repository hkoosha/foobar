package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "Seller",
    url = "\${foobar.url.seller}:\${foobar.port.seller}",
    path = "/foobar/seller/v1",
)
interface SellerApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/seller"]
    )
    fun create(
        @RequestBody
        req: CreateDto,
    ): CreateRespDto

    data class AddressDto(
        val city: String,
        val country: String,
        val zipcode: String,
        val addressLine1: String,
    )

    data class CreateDto(
        val name: String,
        val address: AddressDto,
    )

    data class CreateRespDto(
        val sellerId: UUID,
    )

}
