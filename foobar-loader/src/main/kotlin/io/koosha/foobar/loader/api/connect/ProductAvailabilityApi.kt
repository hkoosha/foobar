package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "ProductAvailability",
    url = "\${foobar.url.warehouse}:\${foobar.port.warehouse}",
    path = "/foobar/warehouse/v1",
)
interface ProductAvailabilityApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/product/{productId}/availability"]
    )
    fun create(
        @PathVariable("productId")
        productId: UUID,

        @RequestBody
        body: CreateDto,
    ): CreateRespDto

    data class CreateDto(
        val sellerId: UUID? = null,
        val unitsAvailable: Long? = null,
        val pricePerUnit: Long? = null,
    )

    data class CreateRespDto(
        val sellerId: UUID,
        val unitsAvailable: Long,
        val frozenUnits: Long,
        val pricePerUnit: Long,
    )

}
