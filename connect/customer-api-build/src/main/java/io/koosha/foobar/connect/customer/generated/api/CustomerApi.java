package io.koosha.foobar.connect.customer.generated.api;

import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.Customer;
import org.openapitools.client.model.CustomerCreateRequest;
import org.openapitools.client.model.CustomerUpdateRequest;
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


public interface CustomerApi extends ApiClient.Api {

  public static final String ENTITY_TYPE = "customer";


  /**
   * 
   * 
   * @param customerId  (required)
   */
  @RequestLine("DELETE /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Accept: */*",
  })
  void deleteCustomer(@Param("customerId") UUID customerId);

  /**
   * 
   * Similar to <code>deleteCustomer</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   */
  @RequestLine("DELETE /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteCustomerWithHttpInfo(@Param("customerId") UUID customerId);



  /**
   * 
   * 
   * @param customerId  (required)
   * @return Customer
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Accept: */*",
  })
  Customer getCustomer(@Param("customerId") UUID customerId);

  /**
   * 
   * Similar to <code>getCustomer</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Customer> getCustomerWithHttpInfo(@Param("customerId") UUID customerId);



  /**
   * 
   * 
   * @return List&lt;Customer&gt;
   */
  @RequestLine("GET /foobar/customer/v1/customers")
  @Headers({
    "Accept: */*",
  })
  List<Customer> getCustomers();

  /**
   * 
   * Similar to <code>getCustomers</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/customer/v1/customers")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Customer>> getCustomersWithHttpInfo();



  /**
   * 
   * 
   * @param customerId  (required)
   * @param customerUpdateRequest  (required)
   * @return Customer
   */
  @RequestLine("PATCH /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Customer patchCustomer(@Param("customerId") UUID customerId, CustomerUpdateRequest customerUpdateRequest);

  /**
   * 
   * Similar to <code>patchCustomer</code> but it also returns the http response headers .
   * 
   * @param customerId  (required)
   * @param customerUpdateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /foobar/customer/v1/customers/{customerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Customer> patchCustomerWithHttpInfo(@Param("customerId") UUID customerId, CustomerUpdateRequest customerUpdateRequest);



  /**
   * 
   * 
   * @param customerCreateRequest  (required)
   * @return Customer
   */
  @RequestLine("POST /foobar/customer/v1/customers")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Customer postCustomer(CustomerCreateRequest customerCreateRequest);

  /**
   * 
   * Similar to <code>postCustomer</code> but it also returns the http response headers .
   * 
   * @param customerCreateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/customer/v1/customers")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Customer> postCustomerWithHttpInfo(CustomerCreateRequest customerCreateRequest);



    public static class Retry implements CustomerApi {

          private CustomerApi api;

          public Retry(CustomerApi api) {
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
          public void deleteCustomer(UUID customerId) {
            try {
                this.api.deleteCustomer(customerId);
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
          public ApiResponse<Void> deleteCustomerWithHttpInfo(UUID customerId) {
            try {
                return this.api.deleteCustomerWithHttpInfo(customerId);
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
          public Customer getCustomer(UUID customerId) {
            try {
                return this.api.getCustomer(customerId);
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
          public ApiResponse<Customer> getCustomerWithHttpInfo(UUID customerId) {
            try {
                return this.api.getCustomerWithHttpInfo(customerId);
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
          public List<Customer> getCustomers() {
            try {
                return this.api.getCustomers();
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
          public ApiResponse<List<Customer>> getCustomersWithHttpInfo() {
            try {
                return this.api.getCustomersWithHttpInfo();
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
          public Customer patchCustomer(UUID customerId, CustomerUpdateRequest customerUpdateRequest) {
            try {
                return this.api.patchCustomer(customerId, customerUpdateRequest);
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
          public ApiResponse<Customer> patchCustomerWithHttpInfo(UUID customerId, CustomerUpdateRequest customerUpdateRequest) {
            try {
                return this.api.patchCustomerWithHttpInfo(customerId, customerUpdateRequest);
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
          public Customer postCustomer(CustomerCreateRequest customerCreateRequest) {
            try {
                return this.api.postCustomer(customerCreateRequest);
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
          public ApiResponse<Customer> postCustomerWithHttpInfo(CustomerCreateRequest customerCreateRequest) {
            try {
                return this.api.postCustomerWithHttpInfo(customerCreateRequest);
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
