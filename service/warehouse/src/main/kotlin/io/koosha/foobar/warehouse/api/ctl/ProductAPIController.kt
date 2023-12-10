package io.koosha.foobar.warehouse.api.ctl

import io.koosha.foobar.warehouse.api.model.dto.ProductCreateRequestDto
import io.koosha.foobar.warehouse.api.model.dto.ProductUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.ProductDO
import io.koosha.foobar.warehouse.api.service.ProductService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
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
import java.util.UUID


@RestController
@RequestMapping("/foobar/warehouse/v1/product")
@Tags(
    Tag(name = "product")
)
class ProductAPIController(
    private val service: ProductService,
) {

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody
        request: ProductCreateRequestDto,

        response: HttpServletResponse,
    ): Product {

        val entity: ProductDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                ProductAPIController::class.java,
                "read",
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

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun readAll(): List<Product> =
        this.service
            .findAll()
            .map(::Product)

    @Transactional(readOnly = true)
    @GetMapping("/{productId}")
    @ResponseBody
    fun read(
        @PathVariable
        productId: UUID,
    ): Product = Product(this.service.findByIdOrFail(productId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PatchMapping("/{productId}")
    @ResponseBody
    fun update(
        @PathVariable
        productId: UUID,

        @RequestBody
        request: ProductUpdateRequestDto,
    ): Product = Product(this.service.update(productId, request))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
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
