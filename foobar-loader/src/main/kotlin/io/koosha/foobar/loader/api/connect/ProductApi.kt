package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "Product",
    url = "\${foobar.url.warehouse}:\${foobar.port.warehouse}",
    path = "/foobar/warehouse/v1",
)
interface ProductApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/product"]
    )
    fun create(
        @RequestBody
        body: CreateDto,
    ): CreateRespDto


    data class CreateDto(
        val name: String,
        val active: Boolean,
        val unitSingle: String,
        val unitMultiple: String,
    )

    data class CreateRespDto(
        val productId: UUID,
        val active: Boolean,
        val name: String,
        val unitMultiple: String,
        val unitSingle: String,
    )

}
