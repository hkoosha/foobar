package io.koosha.foobar.connect.warehouse.generated.api;

import io.koosha.foobar.connect.warehouse.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.generated.api.Availability;
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityCreateRequest;
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityUpdateRequest;
import io.koosha.foobar.connect.warehouse.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityNotFoundApiError;
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
 * API tests for AvailabilityApi
 */
class AvailabilityApiTest {

    private AvailabilityApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(AvailabilityApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteAvailabilityTest() {
        UUID productId = null;
        UUID sellerId = null;
        // api.deleteAvailability(productId, sellerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getAvailabilitiesTest() {
        UUID productId = null;
        // List<Availability> response = api.getAvailabilities(productId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getAvailabilityTest() {
        UUID productId = null;
        UUID sellerId = null;
        // Availability response = api.getAvailability(productId, sellerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void patchAvailabilityTest() {
        UUID productId = null;
        UUID sellerId = null;
        AvailabilityUpdateRequest availabilityUpdateRequest = null;
        // Availability response = api.patchAvailability(productId, sellerId, availabilityUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postAvailabilityTest() {
        UUID productId = null;
        AvailabilityCreateRequest availabilityCreateRequest = null;
        // Availability response = api.postAvailability(productId, availabilityCreateRequest);

        // TODO: test validations
    }

    
}
