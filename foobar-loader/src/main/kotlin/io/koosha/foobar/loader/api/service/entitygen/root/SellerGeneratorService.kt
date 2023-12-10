package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.loader.api.connect.SellerApi
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class SellerGeneratorService(
    private val rand: Rand,
    private val sellerApi: SellerApi,
    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun generate(): Boolean {

        val req = SellerApi.CreateDto(
            name = this.rand.string(10, "name-"),
            address = SellerApi.AddressDto(
                city = this.rand.string(10, "city-"),
                country = this.rand.string(10, "country-"),
                zipcode = this.rand.string(5),
                addressLine1 = this.rand.string(10, "addressLine1-"),
            ),
        )

        val response = try {
            this.sellerApi.create(req)
        }
        catch (e: Exception) {
            log.error("error", e)
            return false
        }

        this.ids.addSeller(response.sellerId)

        return true
    }

}
