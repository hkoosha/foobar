package io.koosha.foobar.connect.seller.generated.api;

import io.koosha.foobar.connect.seller.generated.ApiClient;
import io.koosha.foobar.connect.seller.generated.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.EntityBadValueApiError;
import org.openapitools.client.model.EntityIllegalStateApiError;
import org.openapitools.client.model.EntityNotFoundApiError;
import org.openapitools.client.model.Seller;
import org.openapitools.client.model.SellerCreateRequest;
import org.openapitools.client.model.SellerUpdateRequest;
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


public interface SellerApi extends ApiClient.Api {

  public static final String ENTITY_TYPE = "seller";


  /**
   * 
   * 
   * @param sellerId  (required)
   */
  @RequestLine("DELETE /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  void deleteSeller(@Param("sellerId") UUID sellerId);

  /**
   * 
   * Similar to <code>deleteSeller</code> but it also returns the http response headers .
   * 
   * @param sellerId  (required)
   */
  @RequestLine("DELETE /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Void> deleteSellerWithHttpInfo(@Param("sellerId") UUID sellerId);



  /**
   * 
   * 
   * @param sellerId  (required)
   * @return Seller
   */
  @RequestLine("GET /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  Seller getSeller(@Param("sellerId") UUID sellerId);

  /**
   * 
   * Similar to <code>getSeller</code> but it also returns the http response headers .
   * 
   * @param sellerId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Seller> getSellerWithHttpInfo(@Param("sellerId") UUID sellerId);



  /**
   * 
   * 
   * @return List&lt;Seller&gt;
   */
  @RequestLine("GET /foobar/seller/v1/sellers")
  @Headers({
    "Accept: */*",
  })
  List<Seller> getSellers();

  /**
   * 
   * Similar to <code>getSellers</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /foobar/seller/v1/sellers")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Seller>> getSellersWithHttpInfo();



  /**
   * 
   * 
   * @param sellerId  (required)
   * @param sellerUpdateRequest  (required)
   * @return Seller
   */
  @RequestLine("PATCH /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Seller patchSeller(@Param("sellerId") UUID sellerId, SellerUpdateRequest sellerUpdateRequest);

  /**
   * 
   * Similar to <code>patchSeller</code> but it also returns the http response headers .
   * 
   * @param sellerId  (required)
   * @param sellerUpdateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /foobar/seller/v1/sellers/{sellerId}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Seller> patchSellerWithHttpInfo(@Param("sellerId") UUID sellerId, SellerUpdateRequest sellerUpdateRequest);



  /**
   * 
   * 
   * @param sellerCreateRequest  (required)
   * @return Seller
   */
  @RequestLine("POST /foobar/seller/v1/sellers")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  Seller postSeller(SellerCreateRequest sellerCreateRequest);

  /**
   * 
   * Similar to <code>postSeller</code> but it also returns the http response headers .
   * 
   * @param sellerCreateRequest  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /foobar/seller/v1/sellers")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<Seller> postSellerWithHttpInfo(SellerCreateRequest sellerCreateRequest);



    public static class Retry implements SellerApi {

          private SellerApi api;

          public Retry(SellerApi api) {
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
          public void deleteSeller(UUID sellerId) {
            try {
                this.api.deleteSeller(sellerId);
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
          public ApiResponse<Void> deleteSellerWithHttpInfo(UUID sellerId) {
            try {
                return this.api.deleteSellerWithHttpInfo(sellerId);
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
          public Seller getSeller(UUID sellerId) {
            try {
                return this.api.getSeller(sellerId);
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
          public ApiResponse<Seller> getSellerWithHttpInfo(UUID sellerId) {
            try {
                return this.api.getSellerWithHttpInfo(sellerId);
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
          public List<Seller> getSellers() {
            try {
                return this.api.getSellers();
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
          public ApiResponse<List<Seller>> getSellersWithHttpInfo() {
            try {
                return this.api.getSellersWithHttpInfo();
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
          public Seller patchSeller(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) {
            try {
                return this.api.patchSeller(sellerId, sellerUpdateRequest);
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
          public ApiResponse<Seller> patchSellerWithHttpInfo(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) {
            try {
                return this.api.patchSellerWithHttpInfo(sellerId, sellerUpdateRequest);
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
          public Seller postSeller(SellerCreateRequest sellerCreateRequest) {
            try {
                return this.api.postSeller(sellerCreateRequest);
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
          public ApiResponse<Seller> postSellerWithHttpInfo(SellerCreateRequest sellerCreateRequest) {
            try {
                return this.api.postSellerWithHttpInfo(sellerCreateRequest);
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
