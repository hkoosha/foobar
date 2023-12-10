package io.koosha.foobar.seller.api.service

import io.koosha.foobar.common.error.EntityBadValueException
import io.koosha.foobar.common.error.EntityInIllegalStateException
import io.koosha.foobar.common.error.EntityNotFoundException
import io.koosha.foobar.seller.api.model.SellerState
import io.koosha.foobar.seller.api.model.dto.SellerCreateRequestDto
import io.koosha.foobar.seller.api.model.dto.SellerUpdateRequestDto
import io.koosha.foobar.seller.api.model.entity.SellerDO
import io.koosha.foobar.seller.api.model.repo.SellerRepository
import jakarta.validation.Validator
import net.logstash.logback.argument.StructuredArguments.v
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID


@Service
class SellerService(
    private val sellerRepo: SellerRepository,
    private val validator: Validator,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private fun findSellerOrFail(
        sellerId: UUID,
    ): SellerDO =
        this.sellerRepo
            .findById(sellerId)
            .orElseThrow {
                log.trace("seller not found, sellerId={}", v("sellerId", sellerId))
                EntityNotFoundException(
                    entityType = "seller",
                    entityId = sellerId,
                )
            }

    fun findById(
        id: UUID,
    ): Optional<SellerDO> = this.sellerRepo.findById(id)

    fun findByIdOrFail(
        id: UUID,
    ): SellerDO = this.findSellerOrFail(id)

    fun findAll(): Iterable<SellerDO> =
        this.sellerRepo.findAll()

    fun create(
        request: SellerCreateRequestDto,
    ): SellerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "create seller validation error, request={} errors={}",
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "seller",
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

        log.trace("creating new seller, seller={}", v("seller", seller))
        this.sellerRepo.save(seller)
        log.info("new seller created, seller={}", v("seller", seller))
        return seller
    }

    private fun findAndApplyChanges(
        request: SellerUpdateRequestDto,
        seller: SellerDO,
    ): Boolean {

        var anyChange = false
        val originalSeller = seller.detachedCopy()

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
            log.info(
                "updating seller, seller={} request={}",
                v("seller", originalSeller),
                v("request", request),
            )
        else
            log.trace(
                "nothing to update on seller, seller={}, request={}",
                v("seller", seller),
                v("request", request),
            )

        return anyChange
    }

    fun update(
        sellerId: UUID,
        request: SellerUpdateRequestDto,
    ): SellerDO {

        val errors = this.validator.validate(request)
        if (errors.isNotEmpty()) {
            log.trace(
                "update seller validation error, sellerId={} request={} errors={}",
                v("sellerId", sellerId),
                v("request", request),
                v("validationErrors", errors),
            )
            throw EntityBadValueException(
                entityType = "seller",
                entityId = sellerId,
                errors
            )
        }

        val seller: SellerDO = this.findSellerOrFail(sellerId)

        if (seller.state != SellerState.ACTIVE) {
            log.debug(
                "refused to update seller in current state, seller={} request={}",
                v("seller", seller),
                v("request", request),
            )
            throw EntityInIllegalStateException(
                entityType = "seller",
                entityId = sellerId,
                "seller is not active, can not update"
            )
        }

        val anyChange = this.findAndApplyChanges(request, seller)
        if (!anyChange)
            return seller

        this.sellerRepo.save(seller)
        return seller
    }

    fun delete(
        sellerId: UUID,
    ) {

        val maybeEntity: Optional<SellerDO> = this.findById(sellerId)
        if (!maybeEntity.isPresent) {
            log.debug("not deleting seller, entity does not exist, sellerId={}", v("sellerId", sellerId))
            return
        }

        val seller: SellerDO = maybeEntity.get()

        if (seller.state != SellerState.MARKED_FOR_REMOVAL) {
            log.debug("refused to delete seller in current state, seller={}", v("seller", seller))
            throw EntityInIllegalStateException(
                entityType = "seller",
                entityId = sellerId,
                "deletion not allowed in current state",
            )
        }

        log.info("deleting seller, seller={}", v("seller", seller))
        this.sellerRepo.delete(seller)
    }

}
