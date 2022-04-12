package io.koosha.foobar.seller.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.seller.api.model.SellerDO
import io.koosha.foobar.seller.api.model.SellerRepository
import io.koosha.foobar.seller.api.model.SellerState
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.ZoneOffset
import java.util.*
import javax.validation.Validator


@Service
class SellerServiceImpl(
    private val sellerRepo: SellerRepository,
    private val clock: Clock,
    private val validator: Validator,
) : SellerService {

    private val log = KotlinLogging.logger {}

    private fun findSellerOrFail(sellerId: UUID): SellerDO = this.sellerRepo.findById(sellerId).orElseThrow {
        log.trace { "seller not found, sellerId=$sellerId" }
        EntityNotFoundException(
            entityType = SellerDO.ENTITY_TYPE,
            entityId = sellerId,
        )
    }


    @Transactional(readOnly = true)
    override fun findById(id: UUID): Optional<SellerDO> = this.sellerRepo.findById(id)

    override fun findByIdOrFail(id: UUID): SellerDO = this.findSellerOrFail(id)

    @Transactional(readOnly = true)
    override fun findAll(): Iterable<SellerDO> = this.sellerRepo.findAll()

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun create(request: SellerCreateRequest): SellerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "create seller validation error: $errors" }
            throw EntityBadValueException(
                entityType = SellerDO.ENTITY_TYPE,
                entityId = null,
                errors,
            )
        }

        val seller = SellerDO()
        seller.sellerId = UUID.randomUUID()
        seller.state = SellerState.ACTIVE
        seller.name = request.name
        seller.address.zipcode = request.address!!.zipcode
        seller.address.addressLine1 = request.address.addressLine1
        seller.address.city = request.address.city
        seller.address.country = request.address.country
        seller.created = this.clock.instant().atZone(ZoneOffset.UTC)
        seller.updated = seller.created

        log.info { "creating new seller, seller=$seller" }
        this.sellerRepo.save(seller)

        return seller
    }

    private fun findAndApplyChanges(
        request: SellerUpdateRequest,
        seller: SellerDO,
    ): Boolean {

        var anyChange = false

        if (request.name != null && request.name != seller.name) {
            seller.name = request.name
            anyChange = true
        }
        if (request.address?.addressLine1 != null && request.address.addressLine1 != seller.address.addressLine1) {
            seller.address.addressLine1 = request.address.addressLine1
            anyChange = true
        }
        if (request.address?.zipcode != null && request.address.zipcode != seller.address.zipcode) {
            seller.address.zipcode = request.address.zipcode
            anyChange = true
        }
        if (request.address?.country != null && request.address.country != seller.address.country) {
            seller.address.country = request.address.country
            anyChange = true
        }
        if (request.address?.city != null && request.address.city != seller.address.city) {
            seller.address.city = request.address.city
            anyChange = true
        }

        if (anyChange)
            log.info { "updating seller, sellerId=${seller.sellerId} req=$request" }
        else
            log.trace { "nothing to update on seller, sellerId=${seller.sellerId}, req=$request" }

        return anyChange
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun update(
        sellerId: UUID,
        request: SellerUpdateRequest,
    ): SellerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace { "update seller validation error: $errors" }
            throw EntityBadValueException(
                entityType = SellerDO.ENTITY_TYPE,
                entityId = sellerId,
                errors
            )
        }

        val seller: SellerDO = this.findSellerOrFail(sellerId)
        if (seller.state != SellerState.ACTIVE) {
            log.debug { "refused to update seller in current state, seller=$seller" }
            throw EntityInIllegalStateException(
                entityType = SellerDO.ENTITY_TYPE,
                entityId = sellerId,
                "seller is not active, can not update"
            )
        }

        val anyChange = this.findAndApplyChanges(request, seller)
        if (!anyChange)
            return seller

        log.info { "updating seller, sellerId=$sellerId request=$request" }
        seller.updated = this.clock.instant().atZone(ZoneOffset.UTC)
        this.sellerRepo.save(seller)

        return seller
    }

    @Transactional(
        rollbackForClassName = ["java.lang.Exception"]
    )
    override fun delete(sellerId: UUID) {

        val maybeEntity: Optional<SellerDO> = this.findById(sellerId)
        if (!maybeEntity.isPresent) {
            log.debug { "not deleting seller, entity does not exist, sellerId=$sellerId" }
            return
        }

        val seller: SellerDO = maybeEntity.get()

        if (seller.state != SellerState.MARKED_FOR_REMOVAL) {
            log.debug { "refused to delete seller in current state, seller=$seller" }
            throw EntityInIllegalStateException(
                entityType = SellerDO.ENTITY_TYPE,
                entityId = sellerId,
                "deletion not allowed in current state",
            )
        }

        log.info { "deleting seller, seller=$seller" }
        this.sellerRepo.delete(seller)
    }

}
