package io.koosha.foobar.connect.customer.generated.api;

import io.koosha.foobar.connect.customer.generated.ApiClient;
import org.openapitools.client.model.Customer;
import org.openapitools.client.model.CustomerCreateRequest;
import org.openapitools.client.model.CustomerUpdateRequest;
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
 * API tests for CustomerApi
 */
class CustomerApiTest {

    private CustomerApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CustomerApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteCustomerTest() {
        UUID customerId = null;
        // api.deleteCustomer(customerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getCustomerTest() {
        UUID customerId = null;
        // Customer response = api.getCustomer(customerId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getCustomersTest() {
        // List<Customer> response = api.getCustomers();

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void patchCustomerTest() {
        UUID customerId = null;
        CustomerUpdateRequest customerUpdateRequest = null;
        // Customer response = api.patchCustomer(customerId, customerUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postCustomerTest() {
        CustomerCreateRequest customerCreateRequest = null;
        // Customer response = api.postCustomer(customerCreateRequest);

        // TODO: test validations
    }

    
}
