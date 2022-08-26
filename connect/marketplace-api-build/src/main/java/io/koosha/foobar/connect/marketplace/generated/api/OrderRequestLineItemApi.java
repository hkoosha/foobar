package io.koosha.foobar.connect.marketplace.generated.api;

import io.koosha.foobar.connect.marketplace.generated.ApiClient;
import io.koosha.foobar.connect.marketplace.generated.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.EntityBadValueApiError;
import org.openapitools.client.model.EntityIllegalStateApiError;
import org.openapitools.client.model.EntityNotFoundApiError;
import org.openapitools.client.model.LineItemRequest;
import org.openapitools.client.model.LineItemUpdateRequest;
import org.openapitools.client.model.OrderRequestLineItem;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.TimeoutException;
import java.io.IOException;


import feign.*;

import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Retryable;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;


public interface OrderRequestLineItemApi extends ApiClient.Api {

  String ENTITY_TYPE = "order_request_line_item";


  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   */
  @RequestLine("DELETE /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Accept: */*",
  })
  void deleteLineItem(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId);

  /**
   * 
   * Similar to <code>deleteLineItem</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   */
  @RequestLine("DELETE /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteLineItemWithHttpInfo(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId);



  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   * @return OrderRequestLineItem
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Accept: */*",
  })
  OrderRequestLineItem getLineItem(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId);

  /**
   * 
   * Similar to <code>getLineItem</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<OrderRequestLineItem> getLineItemWithHttpInfo(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId);



  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @return List&lt;OrderRequestLineItem&gt;
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items")
  @Headers({
    "Accept: */*",
  })
  List<OrderRequestLineItem> getLineItems(@Param("orderRequestId") UUID orderRequestId);

  /**
   * 
   * Similar to <code>getLineItems</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<OrderRequestLineItem>> getLineItemsWithHttpInfo(@Param("orderRequestId") UUID orderRequestId);



  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   * @param lineItemUpdateRequest  (required)
   * @return OrderRequestLineItem
   */
  @RequestLine("PATCH /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  OrderRequestLineItem patchLineItem(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest);

  /**
   * 
   * Similar to <code>patchLineItem</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @param orderRequestLineItemId  (required)
   * @param lineItemUpdateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items/{orderRequestLineItemId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<OrderRequestLineItem> patchLineItemWithHttpInfo(@Param("orderRequestId") UUID orderRequestId, @Param("orderRequestLineItemId") Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest);



  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @param lineItemRequest  (required)
   * @return OrderRequestLineItem
   */
  @RequestLine("POST /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  OrderRequestLineItem postLineItem(@Param("orderRequestId") UUID orderRequestId, LineItemRequest lineItemRequest);

  /**
   * 
   * Similar to <code>postLineItem</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @param lineItemRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/marketplace/v1/order-requests/{orderRequestId}/line-items")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<OrderRequestLineItem> postLineItemWithHttpInfo(@Param("orderRequestId") UUID orderRequestId, LineItemRequest lineItemRequest);



  // TODO do annotations work at class level? if yes, move them.
  class Retry implements OrderRequestLineItemApi {

    private final OrderRequestLineItemApi api;

    public Retry(final OrderRequestLineItemApi api) {
      java.util.Objects.requireNonNull(api);
      this.api = api;
    }

    
    @Override
    @CircuitBreaker(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
      resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
      openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
    )
    @Retryable(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
      backoff = @Backoff(
        delayExpression = "${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "${foobar.retry.multiplier:0}",
        randomExpression = "${foobar.retry.random:false}"
      )
    )
    public void deleteLineItem(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        this.api.deleteLineItem(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

      @Override
      @CircuitBreaker(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
        resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
        openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
      )
      @Retryable(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
        backoff = @Backoff(
          delayExpression = "${foobar.retry.delay-millis:1000}",
          maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
          multiplierExpression = "${foobar.retry.multiplier:0}",
          randomExpression = "${foobar.retry.random:false}"
        )
      )
      public ApiResponse<Void> deleteLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId) {
        try {
          return this.api.deleteLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId);
        }
        catch(final NoFallbackAvailableException ex) {
          if(ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          else if(ex.getCause() != null) {
            throw new RuntimeException(ex.getCause());
          }
          else {
            throw ex;
          }
        }
    }



    @Override
    @CircuitBreaker(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
      resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
      openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
    )
    @Retryable(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
      backoff = @Backoff(
        delayExpression = "${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "${foobar.retry.multiplier:0}",
        randomExpression = "${foobar.retry.random:false}"
      )
    )
    public OrderRequestLineItem getLineItem(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        return this.api.getLineItem(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

      @Override
      @CircuitBreaker(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
        resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
        openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
      )
      @Retryable(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
        backoff = @Backoff(
          delayExpression = "${foobar.retry.delay-millis:1000}",
          maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
          multiplierExpression = "${foobar.retry.multiplier:0}",
          randomExpression = "${foobar.retry.random:false}"
        )
      )
      public ApiResponse<OrderRequestLineItem> getLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId) {
        try {
          return this.api.getLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId);
        }
        catch(final NoFallbackAvailableException ex) {
          if(ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          else if(ex.getCause() != null) {
            throw new RuntimeException(ex.getCause());
          }
          else {
            throw ex;
          }
        }
    }



    @Override
    @CircuitBreaker(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
      resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
      openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
    )
    @Retryable(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
      backoff = @Backoff(
        delayExpression = "${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "${foobar.retry.multiplier:0}",
        randomExpression = "${foobar.retry.random:false}"
      )
    )
    public List<OrderRequestLineItem> getLineItems(UUID orderRequestId) {
      try {
        return this.api.getLineItems(orderRequestId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

      @Override
      @CircuitBreaker(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
        resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
        openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
      )
      @Retryable(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
        backoff = @Backoff(
          delayExpression = "${foobar.retry.delay-millis:1000}",
          maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
          multiplierExpression = "${foobar.retry.multiplier:0}",
          randomExpression = "${foobar.retry.random:false}"
        )
      )
      public ApiResponse<List<OrderRequestLineItem>> getLineItemsWithHttpInfo(UUID orderRequestId) {
        try {
          return this.api.getLineItemsWithHttpInfo(orderRequestId);
        }
        catch(final NoFallbackAvailableException ex) {
          if(ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          else if(ex.getCause() != null) {
            throw new RuntimeException(ex.getCause());
          }
          else {
            throw ex;
          }
        }
    }



    @Override
    @CircuitBreaker(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
      resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
      openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
    )
    @Retryable(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
      backoff = @Backoff(
        delayExpression = "${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "${foobar.retry.multiplier:0}",
        randomExpression = "${foobar.retry.random:false}"
      )
    )
    public OrderRequestLineItem patchLineItem(UUID orderRequestId, Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest) {
      try {
        return this.api.patchLineItem(orderRequestId, orderRequestLineItemId, lineItemUpdateRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

      @Override
      @CircuitBreaker(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
        resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
        openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
      )
      @Retryable(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
        backoff = @Backoff(
          delayExpression = "${foobar.retry.delay-millis:1000}",
          maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
          multiplierExpression = "${foobar.retry.multiplier:0}",
          randomExpression = "${foobar.retry.random:false}"
        )
      )
      public ApiResponse<OrderRequestLineItem> patchLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest) {
        try {
          return this.api.patchLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId, lineItemUpdateRequest);
        }
        catch(final NoFallbackAvailableException ex) {
          if(ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          else if(ex.getCause() != null) {
            throw new RuntimeException(ex.getCause());
          }
          else {
            throw ex;
          }
        }
    }



    @Override
    @CircuitBreaker(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
      resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
      openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
    )
    @Retryable(
      include = {
        TimeoutException.class,
        IOException.class,
        FeignException.FeignServerException.class,
      },
      maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
      backoff = @Backoff(
        delayExpression = "${foobar.retry.delay-millis:1000}",
        maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
        multiplierExpression = "${foobar.retry.multiplier:0}",
        randomExpression = "${foobar.retry.random:false}"
      )
    )
    public OrderRequestLineItem postLineItem(UUID orderRequestId, LineItemRequest lineItemRequest) {
      try {
        return this.api.postLineItem(orderRequestId, lineItemRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

      @Override
      @CircuitBreaker(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.circuit-breaker.max-attempts:3}",
        resetTimeoutExpression = "${foobar.circuit-breaker.reset-timeout-millis:20000}",
        openTimeoutExpression = "${foobar.circuit-breaker.open-timeout-millis:5000}"
      )
      @Retryable(
        include = {
          TimeoutException.class,
          IOException.class,
          FeignException.FeignServerException.class,
        },
        maxAttemptsExpression = "${foobar.retry.max-attempts:3}",
        backoff = @Backoff(
          delayExpression = "${foobar.retry.delay-millis:1000}",
          maxDelayExpression = "${foobar.retry.max-delay-millis:0}",
          multiplierExpression = "${foobar.retry.multiplier:0}",
          randomExpression = "${foobar.retry.random:false}"
        )
      )
      public ApiResponse<OrderRequestLineItem> postLineItemWithHttpInfo(UUID orderRequestId, LineItemRequest lineItemRequest) {
        try {
          return this.api.postLineItemWithHttpInfo(orderRequestId, lineItemRequest);
        }
        catch(final NoFallbackAvailableException ex) {
          if(ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          else if(ex.getCause() != null) {
            throw new RuntimeException(ex.getCause());
          }
          else {
            throw ex;
          }
        }
    }


  }

  @Bulkhead(
    name = OrderRequestLineItemApi.ENTITY_TYPE + "_BULKHEAD",
    type = Bulkhead.Type.SEMAPHORE
  )
  class Limit implements OrderRequestLineItemApi {

    private final OrderRequestLineItemApi api;

    public Limit(final OrderRequestLineItemApi api) {
      java.util.Objects.requireNonNull(api);
      this.api = api;
    }

    
    @Override
    public void deleteLineItem(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        this.api.deleteLineItem(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

    @Override
    public ApiResponse<Void> deleteLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        return this.api.deleteLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }



    @Override
    public OrderRequestLineItem getLineItem(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        return this.api.getLineItem(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

    @Override
    public ApiResponse<OrderRequestLineItem> getLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId) {
      try {
        return this.api.getLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }



    @Override
    public List<OrderRequestLineItem> getLineItems(UUID orderRequestId) {
      try {
        return this.api.getLineItems(orderRequestId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

    @Override
    public ApiResponse<List<OrderRequestLineItem>> getLineItemsWithHttpInfo(UUID orderRequestId) {
      try {
        return this.api.getLineItemsWithHttpInfo(orderRequestId);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }



    @Override
    public OrderRequestLineItem patchLineItem(UUID orderRequestId, Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest) {
      try {
        return this.api.patchLineItem(orderRequestId, orderRequestLineItemId, lineItemUpdateRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

    @Override
    public ApiResponse<OrderRequestLineItem> patchLineItemWithHttpInfo(UUID orderRequestId, Long orderRequestLineItemId, LineItemUpdateRequest lineItemUpdateRequest) {
      try {
        return this.api.patchLineItemWithHttpInfo(orderRequestId, orderRequestLineItemId, lineItemUpdateRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }



    @Override
    public OrderRequestLineItem postLineItem(UUID orderRequestId, LineItemRequest lineItemRequest) {
      try {
        return this.api.postLineItem(orderRequestId, lineItemRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }

    @Override
    public ApiResponse<OrderRequestLineItem> postLineItemWithHttpInfo(UUID orderRequestId, LineItemRequest lineItemRequest) {
      try {
        return this.api.postLineItemWithHttpInfo(orderRequestId, lineItemRequest);
      }
      catch(final NoFallbackAvailableException ex) {
        if(ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        else if(ex.getCause() != null) {
          throw new RuntimeException(ex.getCause());
        }
        else {
          throw ex;
        }
      }
    }


  }
}
