package io.koosha.foobar.connect.marketplace.generated.api;

import io.koosha.foobar.connect.marketplace.generated.ApiClient;
import io.koosha.foobar.connect.marketplace.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.marketplace.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.marketplace.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.marketplace.generated.api.LineItemRequest;
import io.koosha.foobar.connect.marketplace.generated.api.LineItemUpdateRequest;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItem;
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
 * API tests for OrderRequestLineItemApi
 */
class OrderRequestLineItemApiTest {

    private OrderRequestLineItemApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(OrderRequestLineItemApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void deleteLineItemTest() {
        UUID orderRequestId = null;
        Long orderRequestLineItemId = null;
        // api.deleteLineItem(orderRequestId, orderRequestLineItemId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getLineItemTest() {
        UUID orderRequestId = null;
        Long orderRequestLineItemId = null;
        // OrderRequestLineItem response = api.getLineItem(orderRequestId, orderRequestLineItemId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getLineItemsTest() {
        UUID orderRequestId = null;
        // List<OrderRequestLineItem> response = api.getLineItems(orderRequestId);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void patchLineItemTest() {
        UUID orderRequestId = null;
        Long orderRequestLineItemId = null;
        LineItemUpdateRequest lineItemUpdateRequest = null;
        // OrderRequestLineItem response = api.patchLineItem(orderRequestId, orderRequestLineItemId, lineItemUpdateRequest);

        // TODO: test validations
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void postLineItemTest() {
        UUID orderRequestId = null;
        LineItemRequest lineItemRequest = null;
        // OrderRequestLineItem response = api.postLineItem(orderRequestId, lineItemRequest);

        // TODO: test validations
    }

    
}
