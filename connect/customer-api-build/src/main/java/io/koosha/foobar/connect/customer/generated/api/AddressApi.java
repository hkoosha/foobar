package io.koosha.foobar.connect.customer.generated.api;

import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.Address;
import org.openapitools.client.model.CustomerAddressCreateRequest;
import org.openapitools.client.model.EntityBadValueApiError;
import org.openapitools.client.model.EntityIllegalStateApiError;
import org.openapitools.client.model.EntityNotFoundApiError;
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


public interface AddressApi extends ApiClient.Api {

  String ENTITY_TYPE = "address";


  /**
   * 
   * 
   * @param customerId  (required)
   * @param addressId  (required)
   */
  @RequestLine("DELETE /foobar/customer/v1/customers/{customerId}/addresses/{addressId}")
  @Headers({
    "Accept: */*",
  })
  void deleteAddress(@Param("customerId") UUID customerId, @Param("addressId") Long addressId);

  /**
   * 
   * Similar to <code>deleteAddress</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @param addressId  (required)
   */
  @RequestLine("DELETE /foobar/customer/v1/customers/{customerId}/addresses/{addressId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteAddressWithHttpInfo(@Param("customerId") UUID customerId, @Param("addressId") Long addressId);



  /**
   * 
   * 
   * @param customerId  (required)
   * @param addressId  (required)
   * @return Address
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}/addresses/{addressId}")
  @Headers({
    "Accept: */*",
  })
  Address getAddress(@Param("customerId") UUID customerId, @Param("addressId") Long addressId);

  /**
   * 
   * Similar to <code>getAddress</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @param addressId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}/addresses/{addressId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Address> getAddressWithHttpInfo(@Param("customerId") UUID customerId, @Param("addressId") Long addressId);



  /**
   * 
   * 
   * @param customerId  (required)
   * @return List&lt;Address&gt;
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}/addresses")
  @Headers({
    "Accept: */*",
  })
  List<Address> getAddresses(@Param("customerId") UUID customerId);

  /**
   * 
   * Similar to <code>getAddresses</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}/addresses")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Address>> getAddressesWithHttpInfo(@Param("customerId") UUID customerId);



  /**
   * 
   * 
   * @param customerId  (required)
   * @param customerAddressCreateRequest  (required)
   * @return Address
   */
  @RequestLine("POST /foobar/customer/v1/customers/{customerId}/addresses")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Address postAddress(@Param("customerId") UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest);

  /**
   * 
   * Similar to <code>postAddress</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @param customerAddressCreateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/customer/v1/customers/{customerId}/addresses")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Address> postAddressWithHttpInfo(@Param("customerId") UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest);



  // TODO do annotations work at class level? if yes, move them.
  class Retry implements AddressApi {

    private final AddressApi api;

    public Retry(final AddressApi api) {
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
    public void deleteAddress(UUID customerId, Long addressId) {
      try {
        this.api.deleteAddress(customerId, addressId);
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
      public ApiResponse<Void> deleteAddressWithHttpInfo(UUID customerId, Long addressId) {
        try {
          return this.api.deleteAddressWithHttpInfo(customerId, addressId);
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
    public Address getAddress(UUID customerId, Long addressId) {
      try {
        return this.api.getAddress(customerId, addressId);
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
      public ApiResponse<Address> getAddressWithHttpInfo(UUID customerId, Long addressId) {
        try {
          return this.api.getAddressWithHttpInfo(customerId, addressId);
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
    public List<Address> getAddresses(UUID customerId) {
      try {
        return this.api.getAddresses(customerId);
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
      public ApiResponse<List<Address>> getAddressesWithHttpInfo(UUID customerId) {
        try {
          return this.api.getAddressesWithHttpInfo(customerId);
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
    public Address postAddress(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) {
      try {
        return this.api.postAddress(customerId, customerAddressCreateRequest);
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
      public ApiResponse<Address> postAddressWithHttpInfo(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) {
        try {
          return this.api.postAddressWithHttpInfo(customerId, customerAddressCreateRequest);
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
    name = AddressApi.ENTITY_TYPE + "_BULKHEAD",
    type = Bulkhead.Type.SEMAPHORE
  )
  class Limit implements AddressApi {

    private final AddressApi api;

    public Limit(final AddressApi api) {
      java.util.Objects.requireNonNull(api);
      this.api = api;
    }

    
    @Override
    public void deleteAddress(UUID customerId, Long addressId) {
      try {
        this.api.deleteAddress(customerId, addressId);
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
    public ApiResponse<Void> deleteAddressWithHttpInfo(UUID customerId, Long addressId) {
      try {
        return this.api.deleteAddressWithHttpInfo(customerId, addressId);
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
    public Address getAddress(UUID customerId, Long addressId) {
      try {
        return this.api.getAddress(customerId, addressId);
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
    public ApiResponse<Address> getAddressWithHttpInfo(UUID customerId, Long addressId) {
      try {
        return this.api.getAddressWithHttpInfo(customerId, addressId);
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
    public List<Address> getAddresses(UUID customerId) {
      try {
        return this.api.getAddresses(customerId);
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
    public ApiResponse<List<Address>> getAddressesWithHttpInfo(UUID customerId) {
      try {
        return this.api.getAddressesWithHttpInfo(customerId);
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
    public Address postAddress(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) {
      try {
        return this.api.postAddress(customerId, customerAddressCreateRequest);
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
    public ApiResponse<Address> postAddressWithHttpInfo(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) {
      try {
        return this.api.postAddressWithHttpInfo(customerId, customerAddressCreateRequest);
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
