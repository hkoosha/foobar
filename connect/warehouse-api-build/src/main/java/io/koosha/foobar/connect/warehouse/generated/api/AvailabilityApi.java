package io.koosha.foobar.connect.warehouse.generated.api;

import io.koosha.foobar.connect.warehouse.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.generated.EncodingUtils;
import io.koosha.foobar.connect.warehouse.generated.api.ApiResponse;

import io.koosha.foobar.connect.warehouse.generated.api.Availability;
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityCreateRequest;
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityUpdateRequest;
import io.koosha.foobar.connect.warehouse.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.warehouse.generated.api.EntityNotFoundApiError;
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


public interface AvailabilityApi extends ApiClient.Api {

  String ENTITY_TYPE = "availability";


  /**
   * 
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   */
  @RequestLine("DELETE /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  void deleteAvailability(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId);

  /**
   * 
   * Similar to <code>deleteAvailability</code> but it also returns the http response headers .
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   */
  @RequestLine("DELETE /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteAvailabilityWithHttpInfo(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId);



  /**
   * 
   * 
   * @param productId  (required)
   * @return List&lt;Availability&gt;
   */
  @RequestLine("GET /foobar/warehouse/v1/products/{productId}/availabilities")
  @Headers({
    "Accept: */*",
  })
  List<Availability> getAvailabilities(@Param("productId") UUID productId);

  /**
   * 
   * Similar to <code>getAvailabilities</code> but it also returns the http response headers .
   * 
   * @param productId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/warehouse/v1/products/{productId}/availabilities")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Availability>> getAvailabilitiesWithHttpInfo(@Param("productId") UUID productId);



  /**
   * 
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   * @return Availability
   */
  @RequestLine("GET /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  Availability getAvailability(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId);

  /**
   * 
   * Similar to <code>getAvailability</code> but it also returns the http response headers .
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Availability> getAvailabilityWithHttpInfo(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId);



  /**
   * 
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   * @param availabilityUpdateRequest  (required)
   * @return Availability
   */
  @RequestLine("PATCH /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Availability patchAvailability(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest);

  /**
   * 
   * Similar to <code>patchAvailability</code> but it also returns the http response headers .
   * 
   * @param productId  (required)
   * @param sellerId  (required)
   * @param availabilityUpdateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Availability> patchAvailabilityWithHttpInfo(@Param("productId") UUID productId, @Param("sellerId") UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest);



  /**
   * 
   * 
   * @param productId  (required)
   * @param availabilityCreateRequest  (required)
   * @return Availability
   */
  @RequestLine("POST /foobar/warehouse/v1/products/{productId}/availabilities")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Availability postAvailability(@Param("productId") UUID productId, AvailabilityCreateRequest availabilityCreateRequest);

  /**
   * 
   * Similar to <code>postAvailability</code> but it also returns the http response headers .
   * 
   * @param productId  (required)
   * @param availabilityCreateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/warehouse/v1/products/{productId}/availabilities")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Availability> postAvailabilityWithHttpInfo(@Param("productId") UUID productId, AvailabilityCreateRequest availabilityCreateRequest);



  // TODO do annotations work at class level? if yes, move them.
  class Retry implements AvailabilityApi {

    private final AvailabilityApi api;

    public Retry(final AvailabilityApi api) {
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
    public void deleteAvailability(UUID productId, UUID sellerId) {
      try {
        this.api.deleteAvailability(productId, sellerId);
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
      public ApiResponse<Void> deleteAvailabilityWithHttpInfo(UUID productId, UUID sellerId) {
        try {
          return this.api.deleteAvailabilityWithHttpInfo(productId, sellerId);
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
    public List<Availability> getAvailabilities(UUID productId) {
      try {
        return this.api.getAvailabilities(productId);
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
      public ApiResponse<List<Availability>> getAvailabilitiesWithHttpInfo(UUID productId) {
        try {
          return this.api.getAvailabilitiesWithHttpInfo(productId);
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
    public Availability getAvailability(UUID productId, UUID sellerId) {
      try {
        return this.api.getAvailability(productId, sellerId);
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
      public ApiResponse<Availability> getAvailabilityWithHttpInfo(UUID productId, UUID sellerId) {
        try {
          return this.api.getAvailabilityWithHttpInfo(productId, sellerId);
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
    public Availability patchAvailability(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) {
      try {
        return this.api.patchAvailability(productId, sellerId, availabilityUpdateRequest);
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
      public ApiResponse<Availability> patchAvailabilityWithHttpInfo(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) {
        try {
          return this.api.patchAvailabilityWithHttpInfo(productId, sellerId, availabilityUpdateRequest);
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
    public Availability postAvailability(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) {
      try {
        return this.api.postAvailability(productId, availabilityCreateRequest);
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
      public ApiResponse<Availability> postAvailabilityWithHttpInfo(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) {
        try {
          return this.api.postAvailabilityWithHttpInfo(productId, availabilityCreateRequest);
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
    name = AvailabilityApi.ENTITY_TYPE + "_BULKHEAD",
    type = Bulkhead.Type.SEMAPHORE
  )
  class Limit implements AvailabilityApi {

    private final AvailabilityApi api;

    public Limit(final AvailabilityApi api) {
      java.util.Objects.requireNonNull(api);
      this.api = api;
    }

    
    @Override
    public void deleteAvailability(UUID productId, UUID sellerId) {
      try {
        this.api.deleteAvailability(productId, sellerId);
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
    public ApiResponse<Void> deleteAvailabilityWithHttpInfo(UUID productId, UUID sellerId) {
      try {
        return this.api.deleteAvailabilityWithHttpInfo(productId, sellerId);
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
    public List<Availability> getAvailabilities(UUID productId) {
      try {
        return this.api.getAvailabilities(productId);
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
    public ApiResponse<List<Availability>> getAvailabilitiesWithHttpInfo(UUID productId) {
      try {
        return this.api.getAvailabilitiesWithHttpInfo(productId);
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
    public Availability getAvailability(UUID productId, UUID sellerId) {
      try {
        return this.api.getAvailability(productId, sellerId);
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
    public ApiResponse<Availability> getAvailabilityWithHttpInfo(UUID productId, UUID sellerId) {
      try {
        return this.api.getAvailabilityWithHttpInfo(productId, sellerId);
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
    public Availability patchAvailability(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) {
      try {
        return this.api.patchAvailability(productId, sellerId, availabilityUpdateRequest);
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
    public ApiResponse<Availability> patchAvailabilityWithHttpInfo(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) {
      try {
        return this.api.patchAvailabilityWithHttpInfo(productId, sellerId, availabilityUpdateRequest);
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
    public Availability postAvailability(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) {
      try {
        return this.api.postAvailability(productId, availabilityCreateRequest);
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
    public ApiResponse<Availability> postAvailabilityWithHttpInfo(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) {
      try {
        return this.api.postAvailabilityWithHttpInfo(productId, availabilityCreateRequest);
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
