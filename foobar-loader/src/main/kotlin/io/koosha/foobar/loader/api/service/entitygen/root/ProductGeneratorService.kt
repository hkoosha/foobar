package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.loader.api.connect.ProductApi
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class ProductGeneratorService(
    private val rand: Rand,
    private val productApi: ProductApi,
    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun generate(): Boolean {

        val req = ProductApi.CreateDto(
            name = this.rand.string(10, "name-"),
            active = true,
            unitSingle = "singular",
            unitMultiple = "multipular"
        )

        val response = try {
            this.productApi.create(req)
        }
        catch (e: Exception) {
            log.error("error", e)
            return false
        }

        this.ids.addProduct(response.productId)

        return true
    }

}
