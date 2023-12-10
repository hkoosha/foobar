package io.koosha.foobar.maker.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
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

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/seller"]
    )
    fun readAll(): Collection<ReadDto>

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/seller/{sellerId}"]
    )
    fun read(
        @PathVariable("sellerId")
        sellerId: UUID,
    ): ReadDto

    @RequestMapping(
        method = [RequestMethod.PATCH],
        path = ["/seller/{sellerId}"]
    )
    fun update(
        @PathVariable("sellerId")
        sellerId: UUID,

        @RequestBody
        req: UpdateDto,
    ): ReadDto

    data class ReadDto(
        val sellerId: UUID,
        val name: String,
        val address: AddressDto,
        val isActive: Boolean,
    )

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

    @Suppress("UNUSED_PARAMETER")
    class UpdateDto(
        name: String?,
        address: AddressUpdateDto?,
    )

    @Suppress("UNUSED_PARAMETER")
    class AddressUpdateDto(
        addressLine1: String?,
        city: String?,
        country: String?,
        zipcode: String?,
    )

}
