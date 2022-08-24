package io.koosha.foobar.shipping.api.service

import feign.FeignException
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.ResourceCurrentlyUnavailableException
import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.shipping.api.model.ShippingDO
import io.koosha.foobar.shipping.api.model.ShippingRepository
import io.koosha.foobar.shipping.api.model.ShippingState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.openapitools.client.model.Address
import org.openapitools.client.model.OrderRequest
import org.openapitools.client.model.Seller
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.validation.Validator


@Service
class ShippingServiceImpl(
    private val sellerClient: SellerApi,
    private val customerAddressClient: AddressApi,
    private val orderRequestClient: OrderRequestApi,
    private val repository: ShippingRepository,
    private val validator: Validator,
) : ShippingService {

    private val log = KotlinLogging.logger {}


    private fun findShippingOrFail(shippingId: UUID): ShippingDO = this.repository.findById(shippingId).orElseThrow {
        log.trace("shipping not found, shipping={}", v("shippingId", shippingId))
        EntityNotFoundException(
            entityType = ShippingDO.ENTITY_TYPE,
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
                entityType = ShippingDO.ENTITY_TYPE,
                entityId = shipping.shippingId,
                msg = "can not set state from=$current to=$target"
            )
        }
    }


    override fun findById(shippingId: UUID): Optional<ShippingDO> = this.repository.findById(shippingId)

    override fun findByIdOrFail(shippingId: UUID): ShippingDO = this.findShippingOrFail(shippingId)

    override fun findAll(): Iterable<ShippingDO> = this.repository.findAll()


    private fun createValidate(
        request: ShippingCreateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create shipping validation error, request={} errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = ShippingDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }
    }

    private fun createFetchSeller(
        request: ShippingCreateRequest,
    ): Seller {

        log.trace("fetching seller, sellerId={}", v("sellerId", request.sellerId))

        val seller = try {
            this.sellerClient.getSeller(request.sellerId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add shipping, seller not found, sellerId={} request={}",
                v("sellerId", request.sellerId),
                v("request", request),
            )
            throw EntityNotFoundException(
                entityType = SellerApi.ENTITY_TYPE,
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
        request: ShippingCreateRequest,
    ): OrderRequest {

        log.trace("fetching orderRequest, orderRequestId={}", v("orderRequestId", request.orderRequestId))

        val orderRequest: OrderRequest = try {
            this.orderRequestClient.getOrderRequest(request.orderRequestId)
        }
        catch (ex: FeignException.NotFound) {
            log.debug(
                "refused to add shipping, orderRequest not found, orderRequestId={} request={}",
                v("orderRequestId", request.orderRequestId),
                v("request", request)
            )
            throw EntityNotFoundException(
                entityType = CustomerApi.ENTITY_TYPE,
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
        request: ShippingCreateRequest,
        orderRequest: OrderRequest,
    ): Address {

        log.trace(
            "fetching customer addresses, customerId={}, orderRequest={}",
            v("customerId", orderRequest.customerId),
            v("orderRequest", orderRequest),
        )

        val customerAddress: List<Address> = try {
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
                entityType = AddressApi.ENTITY_TYPE,
                entityId = null,
                "customer has no address, customerId=${orderRequest.customerId}"
            )
        }

        return customerAddress.last()
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun create(request: ShippingCreateRequest): ShippingDO {

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

        log.info("creating new shipping, shipping={}", v("shipping", shipping))
        this.repository.save(shipping)
        log.info("new shipping crated, shipping={}", v("shipping", shipping))
        return shipping
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun update(
        shippingId: UUID,
        request: ShippingUpdateRequest,
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
                entityType = ShippingDO.ENTITY_TYPE,
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

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun delete(shippingId: UUID) {

        val maybeShipping: Optional<ShippingDO> = this.findById(shippingId)
        if (!maybeShipping.isPresent) {
            log.debug("not deleting shipping, entity does not exist, shippingId={}", v("shippingId", shippingId))
            return
        }

        val shipping: ShippingDO = maybeShipping.get()

        if (shipping.state?.deletionAllowed != true) {
            log.debug("refused to delete shipping in current state, shipping={}", v("shipping", shipping))
            throw EntityInIllegalStateException(
                entityType = ShippingDO.ENTITY_TYPE,
                entityId = shippingId,
            )
        }

        this.repository.delete(shipping)
    }

}
