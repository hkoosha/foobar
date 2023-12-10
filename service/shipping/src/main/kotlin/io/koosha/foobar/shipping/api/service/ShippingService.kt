package io.koosha.foobar.shipping.api.service

import feign.FeignException
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.shipping.api.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.shipping.api.connect.CustomerAddressApi
import io.koosha.foobar.shipping.api.connect.MarketplaceOrderRequestApi
import io.koosha.foobar.shipping.api.connect.SellerApi
import io.koosha.foobar.shipping.api.model.dto.AddressDto
import io.koosha.foobar.shipping.api.model.dto.OrderRequestDto
import io.koosha.foobar.shipping.api.model.dto.SellerDto
import io.koosha.foobar.shipping.api.model.dto.ShippingCreateRequestDto
import io.koosha.foobar.shipping.api.model.dto.ShippingUpdateRequestDto
import io.koosha.foobar.shipping.api.model.entity.ShippingDO
import io.koosha.foobar.shipping.api.model.repo.ShippingRepository
import io.koosha.foobar.shipping.api.model.ShippingState
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID


@Service
class ShippingService(
    private val sellerClient: SellerApi,
    private val customerAddressClient: CustomerAddressApi,
    private val orderRequestClient: MarketplaceOrderRequestApi,
    private val repository: ShippingRepository,
    private val validator: Validator,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun findShippingOrFail(
        shippingId: UUID,
    ): ShippingDO = this.repository.findById(shippingId).orElseThrow {
        log.trace("shipping not found, shipping={}", v("shippingId", shippingId))
        EntityNotFoundException(
            entityType = "shipping",
            entityId = shippingId,
        )
    }

    private fun verifyStateChange(
        shipping: ShippingDO,
        target: ShippingState,
    ) {
        val current = shipping.state ?: throw IllegalArgumentException("state is null: $shipping")

        val ok = when (target) {
            ShippingState.ON_WAY_TO_CUSTOMER -> false
            ShippingState.DELIVERED -> current == ShippingState.ON_WAY_TO_CUSTOMER
        }

        if (!ok) {
            log.debug(
                "illegal shipping state transition, shipping={}, targetState={}",
                v("shipping", shipping),
                v("targetState", target),
            )
            throw EntityInIllegalStateException(
                entityType = "shipping",
                entityId = shipping.shippingId,
                msg = "can not set state from=$current to=$target"
            )
        }
    }

    fun findById(
        shippingId: UUID,
    ): Optional<ShippingDO> = this.repository.findById(shippingId)

    fun findByIdOrFail(
        shippingId: UUID,
    ): ShippingDO = this.findShippingOrFail(shippingId)

    fun findAll(): Iterable<ShippingDO> = this.repository.findAll()

    private fun createValidate(
        request: ShippingCreateRequestDto,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create shipping validation error, request={} errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "shipping",
                entityId = null,
                errors,
            )
        }
    }

    private fun createFetchSeller(
        request: ShippingCreateRequestDto,
    ): SellerDto {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))

        val seller: SellerDto = try {
            this.sellerClient.getSeller(request.sellerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add shipping, seller not found, sellerId={} request={}",
                v("sellerId", request.sellerId),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = "seller",
                entityId = request.sellerId,
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching seller", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }

        return seller
    }

    private fun createFetchOrderRequest(
        request: ShippingCreateRequestDto,
    ): OrderRequestDto {

        log.trace("fetching orderRequest, orderRequestId={}", v("orderRequestId", request.orderRequestId))

        val orderRequest: OrderRequestDto = try {
            this.orderRequestClient.getOrderRequest(request.orderRequestId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add shipping, orderRequest not found, orderRequestId={} request={}",
                v("orderRequestId", request.orderRequestId),
                v("request", request)
            )
            throw EntityNotFoundException(
                entityType = "order_request",
                entityId = request.orderRequestId,
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching orderRequest", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }

        return orderRequest
    }

    private fun createFetchCustomerAddress(
        request: ShippingCreateRequestDto,
        orderRequest: OrderRequestDto,
    ): AddressDto {

        log.trace(
            "fetching customer addresses, customerId={}, orderRequest={}",
            v("customerId", orderRequest.customerId),
            v("orderRequest", orderRequest),
        )

        val customerAddress: List<AddressDto> = try {
            this.customerAddressClient.getAddresses(orderRequest.customerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add shipping, customer not found, customerId={} request={} orderRequest={}",
                v("customerId", orderRequest.customerId),
                v("request", request),
                v("orderRequest", orderRequest),
            )
            throw EntityNotFoundException(
                entityType = "TODO", // extract from exception
                entityId = "TODO", // extract from exception
                ex,
            )
        }
        catch (ex: FeignException.FeignServerException) {
            log.warn("failure while fetching customerAddresses", ex)
            throw ResourceCurrentlyUnavailableException(ex)
        }
        if (customerAddress.isEmpty()) {
            log.debug(
                "refused to add shipping, customer has no address, customerId={} orderRequest={} request={}",
                v("customerId", orderRequest.customerId),
                v("orderRequest", orderRequest),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = "customer_address",
                entityId = null,
                "customer has no address, customerId=${orderRequest.customerId}"
            )
        }

        return customerAddress.last()
    }

    fun create(request: ShippingCreateRequestDto): ShippingDO {

        this.createValidate(request)

        val seller = this.createFetchSeller(request)
        val orderRequest = this.createFetchOrderRequest(request)
        val customerAddress = this.createFetchCustomerAddress(request, orderRequest)

        val shipping = ShippingDO()
        shipping.orderRequestId = request.orderRequestId

        shipping.pickupAddress.zipcode = seller.address.zipcode
        shipping.pickupAddress.city = seller.address.city
        shipping.pickupAddress.country = seller.address.country
        shipping.pickupAddress.addressLine1 = seller.address.addressLine1

        shipping.deliveryAddress.zipcode = customerAddress.zipcode
        shipping.deliveryAddress.city = customerAddress.city
        shipping.deliveryAddress.country = customerAddress.country
        shipping.deliveryAddress.addressLine1 = customerAddress.addressLine1

        shipping.state = ShippingState.ON_WAY_TO_CUSTOMER
        shipping.shippingId = UUID.randomUUID()

        log.trace("creating new shipping, shipping={}", v("shipping", shipping))
        this.repository.save(shipping)
        log.info("new shipping crated, shipping={}", v("shipping", shipping))
        return shipping
    }

    fun update(
        shippingId: UUID,
        request: ShippingUpdateRequestDto,
    ): ShippingDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update shipping validation error, shippingId={} request={} errors={}",
                v("shippingId", shippingId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "shipping",
                entityId = shippingId,
                errors,
            )
        }

        val shipping: ShippingDO = this.findShippingOrFail(shippingId)
        val originalShipping = shipping.detachedCopy()

        var anyChange = false

        if (request.state != null) {
            this.verifyStateChange(shipping, request.state!!)
            shipping.state = request.state
            anyChange = true
        }

        if (!anyChange)
            return shipping

        log.info(
            "updating shipping, shipping={} request={}",
            v("shipping", originalShipping),
            v("request", request)
        )
        this.repository.save(shipping)
        return shipping
    }

    fun delete(shippingId: UUID) {

        val maybeShipping: Optional<ShippingDO> = this.findById(shippingId)
        if (!maybeShipping.isPresent) {
            log.debug("not deleting shipping, entity does not exist, shippingId={}", v("shippingId", shippingId))
            return
        }

        val shipping: ShippingDO = maybeShipping.get()

        if (shipping.state?.deletionAllowed != true) {
            log.debug("refused to delete shipping in current state, shipping={}", v("shipping", shipping))
            throw EntityInIllegalStateException(
                entityType = "shipping",
                entityId = shippingId,
            )
        }

        this.repository.delete(shipping)
    }

}
