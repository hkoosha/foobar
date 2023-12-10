package io.koosha.foobar.maker.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "Customer",
    url = "\${foobar.url.customer}:\${foobar.port.customer}",
    path = "/foobar/customer/v1"
)
interface CustomerApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/customer"]
    )
    fun create(
        @RequestBody
        body: CreateDto,
    ): CreateRespDto

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/customer"]
    )
    fun readAll(): Collection<ReadDto>


    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/customer/{customerId}"]
    )
    fun read(
        @PathVariable("customerId")
        customerId: UUID,
    ): ReadDto

    @RequestMapping(
        method = [RequestMethod.PATCH],
        path = ["/customer/{customerId}"]
    )
    fun update(
        @PathVariable("customerId")
        customerId: UUID,

        @RequestBody
        body: UpdateDto,
    ): ReadDto

    data class CreateRespDto(
        val customerId: UUID,
        val name: NameDto,
        val isActive: Boolean,
    )

    data class CreateDto(
        val name: NameDto,
    )

    data class NameDto(
        val title: Title?,
        val firstName: String?,
        val lastName: String?,
    )

    @Suppress("unused")
    enum class Title {

        MR,
        MS,
        OTHER,
        NOT_SPECIFIED,

        ;

    }

    data class NameUpdateDto(
        val firstName: String?,
        val lastName: String?,
        val title: Title?,
    )

    data class UpdateDto(
        val name: NameUpdateDto,
    )

    data class ReadDto(
        val customerId: UUID,
        val name: NameReadDto,
        val isActive: Boolean,
    )

    data class NameReadDto(
        val title: Title,
        val firstName: String,
        val lastName: String,
    )

}
