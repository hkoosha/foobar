package io.koosha.foobar.loader.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.UUID

@FeignClient(
    name = "OrderRequest",
    url = "\${foobar.url.marketplace}:\${foobar.port.marketplace}",
    path = "/foobar/marketplace/v1",
)
interface OrderRequestApi {

    @RequestMapping(
        method = [RequestMethod.PATCH],
        path = ["/order-request/{orderRequestId}"]
    )
    fun update(
        @PathVariable("orderRequestId")
        orderRequestId: UUID,

        @RequestBody
        body: UpdateDto,
    ): UpdateRespDto

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/order-request"]
    )
    fun create(
        @RequestBody
        body: CreateDto,
    ): CreateRespDto

    data class CreateDto(
        val customerId: UUID,
    )

    data class CreateRespDto(
        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: OrderRequestState,
        val subTotal: Long?,
    )

    data class UpdateDto(
        val sellerId: UUID? = null,
        val state: OrderRequestState? = null,
        val subTotal: Long? = null,
    )

    data class UpdateRespDto(
        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: OrderRequestState,
        val subTotal: Long?,
    )

    @Suppress("unused")
    enum class OrderRequestState {

        ACTIVE,
        LIVE,
        NO_SELLER_FOUND,
        WAITING_FOR_SELLER,
        FULFILLED,
        ;

    }

}
