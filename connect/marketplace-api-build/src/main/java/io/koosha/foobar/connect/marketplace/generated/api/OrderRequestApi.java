package io.koosha.foobar.connect.marketplace.generated.api;

import io.koosha.foobar.connect.marketplace.generated.ApiClient;
import io.koosha.foobar.connect.marketplace.generated.EncodingUtils;
import io.koosha.foobar.connect.marketplace.generated.api.ApiResponse;

import io.koosha.foobar.connect.marketplace.generated.api.ApiError;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequest;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestCreateRequest;
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestUpdateRequest;
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


public interface OrderRequestApi extends ApiClient.Api {

  String ENTITY_TYPE = "order_request";


  /**
   * 
   * 
   * @param orderRequestId  (required)
   */
  @RequestLine("DELETE /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Accept: */*",
  })
  void deleteOrderRequest(@Param("orderRequestId") UUID orderRequestId);

  /**
   * 
   * Similar to <code>deleteOrderRequest</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   */
  @RequestLine("DELETE /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteOrderRequestWithHttpInfo(@Param("orderRequestId") UUID orderRequestId);



  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @return OrderRequest
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Accept: */*",
  })
  OrderRequest getOrderRequest(@Param("orderRequestId") UUID orderRequestId);

  /**
   * 
   * Similar to <code>getOrderRequest</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<OrderRequest> getOrderRequestWithHttpInfo(@Param("orderRequestId") UUID orderRequestId);



  /**
   * 
   * 
   * @param customerId  (optional)
   * @return List&lt;OrderRequest&gt;
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests?customerId={customerId}")
  @Headers({
    "Accept: */*",
  })
  List<OrderRequest> getOrderRequests(@Param("customerId") UUID customerId);

  /**
   * 
   * Similar to <code>getOrderRequests</code> but it also returns the http response headers .
   * 
   * @param customerId  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests?customerId={customerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo(@Param("customerId") UUID customerId);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getOrderRequests</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetOrderRequestsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>customerId -  (optional)</li>
   *   </ul>
   * @return List&lt;OrderRequest&gt;
   */
  @RequestLine("GET /foobar/marketplace/v1/order-requests?customerId={customerId}")
  @Headers({
  "Accept: */*",
  })
  List<OrderRequest> getOrderRequests(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getOrderRequests</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>customerId -  (optional)</li>
      *   </ul>
          * @return List&lt;OrderRequest&gt;
      */
      @RequestLine("GET /foobar/marketplace/v1/order-requests?customerId={customerId}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo(@QueryMap(encoded=true) Map<String, Object> queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getOrderRequests</code> method in a fluent style.
   */
  public static class GetOrderRequestsQueryParams extends HashMap<String, Object> {
    public GetOrderRequestsQueryParams customerId(final UUID value) {
      put("customerId", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param orderRequestId  (required)
   * @param orderRequestUpdateRequest  (required)
   * @return OrderRequest
   */
  @RequestLine("PATCH /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  OrderRequest patchOrderRequest(@Param("orderRequestId") UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest);

  /**
   * 
   * Similar to <code>patchOrderRequest</code> but it also returns the http response headers .
   * 
   * @param orderRequestId  (required)
   * @param orderRequestUpdateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /foobar/marketplace/v1/order-requests/{orderRequestId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<OrderRequest> patchOrderRequestWithHttpInfo(@Param("orderRequestId") UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest);



  /**
   * 
   * 
   * @param orderRequestCreateRequest  (required)
   * @return OrderRequest
   */
  @RequestLine("POST /foobar/marketplace/v1/order-requests")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  OrderRequest postOrderRequest(OrderRequestCreateRequest orderRequestCreateRequest);

  /**
   * 
   * Similar to <code>postOrderRequest</code> but it also returns the http response headers .
   * 
   * @param orderRequestCreateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/marketplace/v1/order-requests")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<OrderRequest> postOrderRequestWithHttpInfo(OrderRequestCreateRequest orderRequestCreateRequest);



  // TODO do annotations work at class level? if yes, move them.
  class Retry implements OrderRequestApi {

    private final OrderRequestApi api;

    public Retry(final OrderRequestApi api) {
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
    public void deleteOrderRequest(UUID orderRequestId) {
      try {
        this.api.deleteOrderRequest(orderRequestId);
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
      public ApiResponse<Void> deleteOrderRequestWithHttpInfo(UUID orderRequestId) {
        try {
          return this.api.deleteOrderRequestWithHttpInfo(orderRequestId);
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
    public OrderRequest getOrderRequest(UUID orderRequestId) {
      try {
        return this.api.getOrderRequest(orderRequestId);
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
      public ApiResponse<OrderRequest> getOrderRequestWithHttpInfo(UUID orderRequestId) {
        try {
          return this.api.getOrderRequestWithHttpInfo(orderRequestId);
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
    public List<OrderRequest> getOrderRequests(UUID customerId) {
      try {
        return this.api.getOrderRequests(customerId);
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
      public ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo(UUID customerId) {
        try {
          return this.api.getOrderRequestsWithHttpInfo(customerId);
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
    public List<OrderRequest> getOrderRequests( Map<String, Object> queryParams) {
      return  this.api.getOrderRequests( queryParams);
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
    public ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo( Map<String, Object> queryParams) {
      return this.api.getOrderRequestsWithHttpInfo( queryParams);
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
    public OrderRequest patchOrderRequest(UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest) {
      try {
        return this.api.patchOrderRequest(orderRequestId, orderRequestUpdateRequest);
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
      public ApiResponse<OrderRequest> patchOrderRequestWithHttpInfo(UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest) {
        try {
          return this.api.patchOrderRequestWithHttpInfo(orderRequestId, orderRequestUpdateRequest);
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
    public OrderRequest postOrderRequest(OrderRequestCreateRequest orderRequestCreateRequest) {
      try {
        return this.api.postOrderRequest(orderRequestCreateRequest);
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
      public ApiResponse<OrderRequest> postOrderRequestWithHttpInfo(OrderRequestCreateRequest orderRequestCreateRequest) {
        try {
          return this.api.postOrderRequestWithHttpInfo(orderRequestCreateRequest);
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
    name = OrderRequestApi.ENTITY_TYPE + "_BULKHEAD",
    type = Bulkhead.Type.SEMAPHORE
  )
  class Limit implements OrderRequestApi {

    private final OrderRequestApi api;

    public Limit(final OrderRequestApi api) {
      java.util.Objects.requireNonNull(api);
      this.api = api;
    }

    
    @Override
    public void deleteOrderRequest(UUID orderRequestId) {
      try {
        this.api.deleteOrderRequest(orderRequestId);
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
    public ApiResponse<Void> deleteOrderRequestWithHttpInfo(UUID orderRequestId) {
      try {
        return this.api.deleteOrderRequestWithHttpInfo(orderRequestId);
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
    public OrderRequest getOrderRequest(UUID orderRequestId) {
      try {
        return this.api.getOrderRequest(orderRequestId);
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
    public ApiResponse<OrderRequest> getOrderRequestWithHttpInfo(UUID orderRequestId) {
      try {
        return this.api.getOrderRequestWithHttpInfo(orderRequestId);
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
    public List<OrderRequest> getOrderRequests(UUID customerId) {
      try {
        return this.api.getOrderRequests(customerId);
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
    public ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo(UUID customerId) {
      try {
        return this.api.getOrderRequestsWithHttpInfo(customerId);
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
    public List<OrderRequest> getOrderRequests( Map<String, Object> queryParams) {
      return  this.api.getOrderRequests( queryParams);
    }

    @Override
    public ApiResponse<List<OrderRequest>> getOrderRequestsWithHttpInfo( Map<String, Object> queryParams) {
      return this.api.getOrderRequestsWithHttpInfo( queryParams);
    }



    @Override
    public OrderRequest patchOrderRequest(UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest) {
      try {
        return this.api.patchOrderRequest(orderRequestId, orderRequestUpdateRequest);
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
    public ApiResponse<OrderRequest> patchOrderRequestWithHttpInfo(UUID orderRequestId, OrderRequestUpdateRequest orderRequestUpdateRequest) {
      try {
        return this.api.patchOrderRequestWithHttpInfo(orderRequestId, orderRequestUpdateRequest);
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
    public OrderRequest postOrderRequest(OrderRequestCreateRequest orderRequestCreateRequest) {
      try {
        return this.api.postOrderRequest(orderRequestCreateRequest);
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
    public ApiResponse<OrderRequest> postOrderRequestWithHttpInfo(OrderRequestCreateRequest orderRequestCreateRequest) {
      try {
        return this.api.postOrderRequestWithHttpInfo(orderRequestCreateRequest);
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
