package io.koosha.foobar.customer.api.service

import io.koosha.foobar.common.RandomUUIDProvider
import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.common.error.anyOfMessagesContains
import io.koosha.foobar.common.model.EntityInfo
import io.koosha.foobar.customer.api.model.AddressDO
import io.koosha.foobar.customer.api.model.AddressRepository
import io.koosha.foobar.customer.api.model.CustomerDO
import io.koosha.foobar.customer.api.model.CustomerRepository
import io.koosha.foobar.customer.api.model.CustomerState
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.v
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.util.*
import javax.validation.Validator


@Service
class CustomerServiceImpl(
    private val validator: Validator,
    private val randomUUIDProvider: RandomUUIDProvider,

    private val customerRepo: CustomerRepository,
    private val addressRepo: AddressRepository,

    private val txTemplate: TransactionTemplate,
) : CustomerService {

    private val log = KotlinLogging.logger {}

    @Transactional(readOnly = true)
    override fun findByCustomerIdOrFail(customerId: UUID): CustomerDO =
        this.customerRepo.findById(customerId).orElseThrow {
            log.trace("customer not found, customerId={}", v("customerId", customerId))
            EntityNotFoundException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = customerId,
            )
        }

    @Transactional(readOnly = true)
    override fun findById(customerId: UUID): Optional<CustomerDO> = this.customerRepo.findById(customerId)

    @Transactional(readOnly = true)
    override fun findAll(): Iterable<CustomerDO> = this.customerRepo.findAll()

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun create(request: CustomerCreateRequest): CustomerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create customer validation error, request={}, errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = null,
                errors = errors,
            )
        }

        val customer = this.create0(request)

        log.info("creating new customer={}", v("customer", customer))
        val saved = this.customerRepo.save(customer)
        log.info("new customer created={}", v("customer", saved))
        return saved
    }

    internal fun create0(request: CustomerCreateRequest): CustomerDO {

        val customer = CustomerDO()
        customer.customerId = this.randomUUIDProvider.randomUUID()
        customer.state = CustomerState.ACTIVE
        customer.name.title = request.name!!.title
        customer.name.firstName = request.name.firstName
        customer.name.lastName = request.name.lastName
        customer.addressIdPool = 0

        return customer
    }

    internal fun findAndApplyChanges(
        request: CustomerUpdateRequest,
        customer: CustomerDO,
    ): Boolean {

        var anyChange = false
        val originalCustomer = customer.detachedCopy()

        if (request.name?.title != null && customer.name.title != request.name.title) {
            anyChange = true
            customer.name.title = request.name.title
        }
        if (request.name?.firstName != null && customer.name.firstName != request.name.firstName) {
            anyChange = true
            customer.name.firstName = request.name.firstName
        }
        if (request.name?.lastName != null && customer.name.lastName != request.name.lastName) {
            anyChange = true
            customer.name.lastName = request.name.lastName
        }

        if (anyChange)
            log.info(
                "updating customer, customer={}, request={}",
                v("customer", originalCustomer),
                v("request", request),
            )
        else
            log.trace(
                "nothing to update on customer, customer={}, request={}",
                v("customer", customer),
                v("request", request)
            )

        return anyChange
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun update(
        customerId: UUID,
        request: CustomerUpdateRequest,
    ): CustomerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update customer validation error, customerId={} request={}, errors={}",
                v("customerId", customerId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = null,
                errors = errors,
            )
        }

        val customer: CustomerDO = this.findByCustomerIdOrFail(customerId)
        if (customer.state != CustomerState.ACTIVE) {
            log.debug("refused to update customer in current state, customer={}", v("customer", customer))
            throw EntityInIllegalStateException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = customerId,
                msg = "customer is not active, can not update"
            )
        }

        val anyChange = this.findAndApplyChanges(request, customer)
        if (!anyChange)
            return customer

        val updated = this.customerRepo.save(customer)
        return updated
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun delete(customerId: UUID) {

        val maybeCustomer: Optional<CustomerDO> = this.customerRepo.findById(customerId)
        if (maybeCustomer.isEmpty) {
            log.debug("not deleting customer, entity does not exist, customerId={}", v("customerId", customerId))
            return
        }

        val customer: CustomerDO = maybeCustomer.get()

        if (customer.state != CustomerState.MARKED_FOR_REMOVAL) {
            log.debug("refused to delete customer in current state, customer={}", v("customer", customer))
            throw EntityInIllegalStateException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = customerId,
                msg = "deletion not allowed in current state"
            )
        }

        log.info("deleting customer and addresses, customer={}", v("customer", customer))
        this.addressRepo.deleteByAddressPk_Customer_customerId(customer.customerId!!)
        this.customerRepo.delete(customer)
    }


    private fun addAddressValidate(
        customerId: UUID,
        request: CustomerAddressCreateRequest,
    ) {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "add address validation error, customerId={}, request={}, errors={}",
                v("customerId", customerId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = AddressDO.ENTITY_TYPE,
                entityId = null,
                errors = errors,
            )
        }
    }

    private fun addAddressGetCustomer(
        customerId: UUID,
        request: CustomerAddressCreateRequest,
    ): CustomerDO {

        val customer: CustomerDO = this.findByCustomerIdOrFail(customerId)
        if (customer.state != CustomerState.ACTIVE) {
            log.debug(
                "refused to add address in current state of customer, customer={}, request={}",
                v("customer", customer),
                v("request", request)
            )
            throw EntityInIllegalStateException(
                entityType = CustomerDO.ENTITY_TYPE,
                entityId = customerId,
                msg = "customer is not active, can not add address"
            )
        }

        return customer
    }

    override fun addAddress(
        customerId: UUID,
        request: CustomerAddressCreateRequest,
    ): AddressDO {

        this.addAddressValidate(customerId, request)

        val customer: CustomerDO = this.addAddressGetCustomer(customerId, request)

        val addressId: Long = customer.addressIdPool!! + 1
        customer.addressIdPool = addressId

        val address = AddressDO()
        address.addressPk.addressId = addressId
        address.addressPk.customer = customer
        address.zipcode = request.zipcode
        address.country = request.country
        address.city = request.city
        address.addressLine1 = request.addressLine1
        address.name = request.name

        log.info(
            "adding customer address, customer={} address={}",
            v("customer", customer),
            v("address", address)
        )

        val saved = try {
            this.txTemplate.execute {
                this.customerRepo.save(customer)
                this.addressRepo.save(address)
            }
        }
        catch (e: DataIntegrityViolationException) {
            if (anyOfMessagesContains(e, "Duplicate entry"))
                throw EntityBadValueException(
                    context = setOf(
                        EntityInfo(
                            entityType = CustomerDO.ENTITY_TYPE,
                            entityId = customerId,
                        ),
                        EntityInfo(
                            entityType = AddressDO.ENTITY_TYPE,
                            entityId = addressId,
                        ),
                    ),
                    msg = "duplicate entry for availability"
                )
            else
                throw e
        }

        log.info(
            "new customer address saved, customer={} address={}",
            v("customer", customer),
            v("address", address)
        )

        return saved!!
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun deleteAddress(
        customerId: UUID,
        addressId: Long,
    ) {

        val customer: CustomerDO = this.findByCustomerIdOrFail(customerId)

        if (customer.state != CustomerState.ACTIVE) {
            log.debug(
                "refused to delete address in current state of customer, customer={}, addressId={}",
                v("customer", customer),
                v("addressId", addressId)
            )
            throw EntityInIllegalStateException(
                context = setOf(
                    EntityInfo(
                        entityType = CustomerDO.ENTITY_TYPE,
                        entityId = customerId,
                    ),
                    EntityInfo(
                        entityType = AddressDO.ENTITY_TYPE,
                        entityId = addressId,
                    ),
                ),
                msg = "customer is not active, can not delete address",
            )
        }

        val address = this.addressRepo.findByAddressPk(
            AddressDO.Pk(
                addressId = addressId,
                customer = customer,
            ),
        )

        if (address.isEmpty) {
            log.debug(
                "not deleting address, entity does not exist, customer={}, addressId={}",
                v("customer", customer),
                v("addressId", addressId),
            )
            return
        }

        log.debug(
            "removing customer address, customer={} address={}",
            v("customer", customer),
            v("address", address),
        )
        this.addressRepo.delete(address.get())
    }

    @Transactional(readOnly = true)
    override fun getAddressesOfCustomer(customerId: UUID): Iterable<AddressDO> {

        val customer: CustomerDO = this.findByCustomerIdOrFail(customerId)
        val find: Iterable<AddressDO> = this.addressRepo.findByAddressPk_Customer(customer)
        return find
    }

    @Transactional(readOnly = true)
    override fun getAddress(
        customerId: UUID,
        addressId: Long,
    ): AddressDO {

        val customer = this.findByCustomerIdOrFail(customerId)
        return addressRepo.findByAddressPk_Customer_customerIdAndAddressPk_addressId(customer.customerId!!, addressId)
            .orElseThrow {
                log.trace(
                    "customer address not found, customer={}, addressId={}",
                    v("customer", customer),
                    v("addressId", addressId),
                )
                EntityNotFoundException(
                    context = setOf(
                        EntityInfo(
                            entityType = CustomerDO.ENTITY_TYPE,
                            entityId = customerId,
                        ),
                        EntityInfo(
                            entityType = AddressDO.ENTITY_TYPE,
                            entityId = addressId,
                        ),
                    ),
                )
            }
    }

}
