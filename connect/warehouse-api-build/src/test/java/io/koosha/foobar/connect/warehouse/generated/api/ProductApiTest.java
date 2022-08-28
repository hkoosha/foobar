package io.koosha.foobar.connect.warehouse.generated.api;

import io.koosha.foobar.connect.warehouse.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.warehouse.generated.api.Product;
import io.koosha.foobar.connect.warehouse.generated.api.ProductCreateRequest;
import io.koosha.foobar.connect.warehouse.generated.api.ProductUpdateRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ProductApi
 */
class ProductApiTest {

    private ProductApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(ProductApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteProductTest() {
        UUID productId = null;
        // api.deleteProduct(productId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getProductTest() {
        UUID productId = null;
        // Product response = api.getProduct(productId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getProductsTest() {
        // List<Product> response = api.getProducts();

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void patchProductTest() {
        UUID productId = null;
        ProductUpdateRequest productUpdateRequest = null;
        // Product response = api.patchProduct(productId, productUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postProductTest() {
        ProductCreateRequest productCreateRequest = null;
        // Product response = api.postProduct(productCreateRequest);

        // TODO: test validations
    }

    
}
