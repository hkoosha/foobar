package io.koosha.foobar.warehouse.api.ctl

import io.koosha.foobar.warehouse.API_PATH_PREFIX
import io.koosha.foobar.warehouse.api.model.ProductDO
import io.koosha.foobar.warehouse.api.service.ProductCreateRequest
import io.koosha.foobar.warehouse.api.service.ProductService
import io.koosha.foobar.warehouse.api.service.ProductUpdateRequest
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping(ProductAPIController.URI)
@Tags(
    Tag(name = ProductDO.ENTITY_TYPE)
)
class ProductAPIController(
    private val service: ProductService,
) {

    companion object {

        const val URI = "/$API_PATH_PREFIX/products"
        const val URI__PART__PRODUCT_ID = "productId"

    }

    @GetMapping
    @ResponseBody
    fun getProducts(): List<Product> = service.findAll().map(::Product)

    @GetMapping("/{$URI__PART__PRODUCT_ID}")
    @ResponseBody
    fun getProduct(
        @PathVariable
        productId: UUID,
    ): Product = Product(service.findByIdOrFail(productId))

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(
        @RequestBody
        request: ProductCreateRequest,
        response: HttpServletResponse,
    ): Product {

        val entity: ProductDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                ProductAPIController::class.java,
                "getProduct",
                entity.productId,
            )
            .buildAndExpand(
                entity.productId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return Product(entity)
    }

    @PatchMapping("/{$URI__PART__PRODUCT_ID}")
    @ResponseBody
    fun patchProduct(
        @PathVariable
        productId: UUID,
        @RequestBody
        request: ProductUpdateRequest,
    ): Product = Product(service.update(productId, request))

    @DeleteMapping("/{$URI__PART__PRODUCT_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @PathVariable
        productId: UUID,
    ) = this.service.delete(productId)


    data class Product(
        val productId: UUID,
        val active: Boolean,
        val name: String,
        val unitMultiple: String,
        val unitSingle: String,
    ) {

        constructor(entity: ProductDO) : this(

            productId = entity.productId!!,
            active = entity.active!!,
            name = entity.name!!,
            unitMultiple = entity.unitMultiple!!,
            unitSingle = entity.unitSingle!!,
        )
    }
}
