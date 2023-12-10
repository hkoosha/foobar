package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "OrderRequestLineItem",
    url = "\${foobar.url.marketplace}:\${foobar.port.marketplace}",
    path = "/foobar/marketplace/v1",
)
interface OrderRequestLineItemApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/order-request/{orderRequestId}/line-item"]
    )
    fun create(
        @PathVariable("orderRequestId")
        orderRequestId: UUID,

        @RequestBody
        body: CreateDto,
    ): CreateRespDto

    data class CreateDto(
        val productId: UUID?,
        val units: Long?,
    )

    data class CreateRespDto(
        val orderRequestId: UUID,
        val lineItemId: Long,
        val productId: UUID,
        val units: Long,
    )

}
