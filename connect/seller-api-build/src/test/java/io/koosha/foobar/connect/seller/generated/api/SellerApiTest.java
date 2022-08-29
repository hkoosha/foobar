package io.koosha.foobar.connect.seller.generated.api;

import io.koosha.foobar.connect.seller.generated.ApiClient;
import io.koosha.foobar.connect.seller.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.seller.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.seller.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.seller.generated.api.Seller;
import io.koosha.foobar.connect.seller.generated.api.SellerCreateRequest;
import io.koosha.foobar.connect.seller.generated.api.SellerUpdateRequest;
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
 * API tests for SellerApi
 */
class SellerApiTest {

    private SellerApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(SellerApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteSellerTest() {
        UUID sellerId = null;
        // api.deleteSeller(sellerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getSellerTest() {
        UUID sellerId = null;
        // Seller response = api.getSeller(sellerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getSellersTest() {
        // List<Seller> response = api.getSellers();

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void patchSellerTest() {
        UUID sellerId = null;
        SellerUpdateRequest sellerUpdateRequest = null;
        // Seller response = api.patchSeller(sellerId, sellerUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postSellerTest() {
        SellerCreateRequest sellerCreateRequest = null;
        // Seller response = api.postSeller(sellerCreateRequest);

        // TODO: test validations
    }

    
}
