package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.seller.generated.api.SellerCreateRequest
import io.koosha.foobar.connect.seller.generated.api.SellerCreateRequestAddress
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import io.koosha.foobar.loader.api.service.Rand
import mu.KotlinLogging
import org.springframework.stereotype.Service


@Service
class SellerGeneratorService(
    private val rand: Rand,

    private val sellerApi: SellerApi,

    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log = KotlinLogging.logger {}


    override fun generate(): Boolean {

        val req = SellerCreateRequest()
        req.name = this.rand.string(10, "name-")
        req.address = SellerCreateRequestAddress()
        req.address.city = this.rand.string(10, "city-")
        req.address.country = this.rand.string(10, "country-")
        req.address.zipcode = this.rand.string(5)
        req.address.addressLine1 = this.rand.string(10, "addressLine1-")

        val response = this.sellerApi.postSellerWithHttpInfo(req)
        if (response.statusCode < 200 || response.statusCode > 299)
            return false

        val entity = response.data
        this.ids.addSeller(entity.sellerId)

        return true
    }

}
