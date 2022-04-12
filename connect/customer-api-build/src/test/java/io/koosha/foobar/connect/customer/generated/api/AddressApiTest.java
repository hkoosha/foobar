package io.koosha.foobar.connect.customer.generated.api;

import io.koosha.foobar.connect.customer.generated.ApiClient;
import org.openapitools.client.model.Address;
import org.openapitools.client.model.CustomerAddressCreateRequest;
import org.openapitools.client.model.EntityBadValueApiError;
import org.openapitools.client.model.EntityIllegalStateApiError;
import org.openapitools.client.model.EntityNotFoundApiError;
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
 * API tests for AddressApi
 */
class AddressApiTest {

    private AddressApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(AddressApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteAddressTest() {
        UUID customerId = null;
        Long addressId = null;
        // api.deleteAddress(customerId, addressId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getAddressTest() {
        UUID customerId = null;
        Long addressId = null;
        // Address response = api.getAddress(customerId, addressId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getAddressesTest() {
        UUID customerId = null;
        // List<Address> response = api.getAddresses(customerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postAddressTest() {
        UUID customerId = null;
        CustomerAddressCreateRequest customerAddressCreateRequest = null;
        // Address response = api.postAddress(customerId, customerAddressCreateRequest);

        // TODO: test validations
    }

    
}
