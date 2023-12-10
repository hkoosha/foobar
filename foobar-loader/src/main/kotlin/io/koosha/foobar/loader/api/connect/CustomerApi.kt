package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "Customer",
    url = "\${foobar.url.customer}:\${foobar.port.customer}",
    path = "/foobar/customer/v1",
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

}
