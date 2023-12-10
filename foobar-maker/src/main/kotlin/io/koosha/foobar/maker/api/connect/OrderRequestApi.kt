package io.koosha.foobar.maker.api.connect

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(
    name = "OrderRequest",
    url = "\${foobar.url.marketplace}:\${foobar.port.marketplace}",
    path = "/foobar/marketplace/v1",
)
interface OrderRequestApi {

    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/order-request"]
    )
    fun create(
        @RequestBody
        body: CreateDto,
    ): CreateRespDto

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/order-request"]
    )
    fun readAllForCustomer(
        @RequestParam("customerId")
        customerId: UUID,
    ): Collection<ReadDto>

    @RequestMapping(
        method = [RequestMethod.GET],
        path = ["/order-request/{orderRequestId}"]
    )
    fun read(
        @PathVariable("orderRequestId")
        orderRequestId: UUID,
    ): ReadDto

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

    data class ReadDto(
        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: State,
        val subTotal: Long?,
    )

    data class CreateDto(
        val customerId: UUID,
    )

    data class CreateRespDto(
        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: State,
        val subTotal: Long?,
    )

    data class UpdateDto(
        val sellerId: UUID? = null,
        val state: State? = null,
        val subTotal: Long? = null,
    )

    data class UpdateRespDto(
        val orderRequestId: UUID,
        val customerId: UUID,
        val sellerId: UUID?,
        val state: State,
        val subTotal: Long?,
    )

    @Suppress("unused")
    enum class State {

        ACTIVE,
        LIVE,
        NO_SELLER_FOUND,
        WAITING_FOR_SELLER,
        FULFILLED,
        ;

    }

}
