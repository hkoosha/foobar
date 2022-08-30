package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductCreateRequest
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import io.koosha.foobar.loader.api.service.Rand
import mu.KotlinLogging
import org.springframework.stereotype.Service


@Service
class ProductGeneratorService(
    private val rand: Rand,

    private val productApi: ProductApi,

    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log = KotlinLogging.logger {}


    override fun generate(): Boolean {

        val req = ProductCreateRequest()
        req.name = this.rand.string(10, "name-")
        req.active = true
        req.unitSingle = "singular"
        req.unitMultiple = "multipular"

        val response = this.productApi.postProductWithHttpInfo(req)
        if (response.statusCode < 200 || response.statusCode > 299)
            return false

        val entity = response.data
        this.ids.addProduct(entity.productId)

        return true
    }

}
