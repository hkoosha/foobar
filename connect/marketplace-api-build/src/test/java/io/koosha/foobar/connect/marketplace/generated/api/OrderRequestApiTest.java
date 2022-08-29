package io.koosha.foobar.connect.marketplace.generated.api;

import io.koosha.foobar.connect.marketplace.generated.ApiClient;
import io.koosha.foobar.connect.marketplace.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.marketplace.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.marketplace.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequest;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestCreateRequest;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestUpdateRequest;
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
 * API tests for OrderRequestApi
 */
class OrderRequestApiTest {

    private OrderRequestApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(OrderRequestApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteOrderRequestTest() {
        UUID orderRequestId = null;
        // api.deleteOrderRequest(orderRequestId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getOrderRequestTest() {
        UUID orderRequestId = null;
        // OrderRequest response = api.getOrderRequest(orderRequestId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getOrderRequestsTest() {
        UUID customerId = null;
        // List<OrderRequest> response = api.getOrderRequests(customerId);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     *
     * This tests the overload of the method that uses a Map for query parameters instead of
     * listing them out individually.
     */
    @Test
    void getOrderRequestsTestQueryMap() {
        OrderRequestApi.GetOrderRequestsQueryParams queryParams = new OrderRequestApi.GetOrderRequestsQueryParams()
            .customerId(null);
        // List<OrderRequest> response = api.getOrderRequests(queryParams);

    // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     */
    @Test
    void patchOrderRequestTest() {
        UUID orderRequestId = null;
        OrderRequestUpdateRequest orderRequestUpdateRequest = null;
        // OrderRequest response = api.patchOrderRequest(orderRequestId, orderRequestUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postOrderRequestTest() {
        OrderRequestCreateRequest orderRequestCreateRequest = null;
        // OrderRequest response = api.postOrderRequest(orderRequestCreateRequest);

        // TODO: test validations
    }

    
}
