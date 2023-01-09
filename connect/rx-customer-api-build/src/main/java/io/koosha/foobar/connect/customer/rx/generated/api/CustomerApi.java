package io.koosha.foobar.connect.customer.rx.generated.api;

import io.koosha.foobar.connect.customer.rx.generated.ApiClient;

import io.koosha.foobar.connect.customer.rx.generated.api.Customer;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerCreateRequest;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerUpdateRequest;
import io.koosha.foobar.connect.customer.rx.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.customer.rx.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.customer.rx.generated.api.EntityNotFoundApiError;
import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class CustomerApi {
    private ApiClient apiClient;

    public CustomerApi() {
        this(new ApiClient());
    }

    @Autowired
    public CustomerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param customerId The customerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteCustomerRequestCreation(UUID customerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling deleteCustomer", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("customerId", customerId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param customerId The customerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteCustomer(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteCustomerRequestCreation(customerId).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param customerId The customerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> deleteCustomerWithHttpInfo(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteCustomerRequestCreation(customerId).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param customerId The customerId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec deleteCustomerWithResponseSpec(UUID customerId) throws WebClientResponseException {
        return deleteCustomerRequestCreation(customerId);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getCustomerRequestCreation(UUID customerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling getCustomer", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("customerId", customerId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Customer> getCustomer(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return getCustomerRequestCreation(customerId).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @return ResponseEntity&lt;Customer&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Customer>> getCustomerWithHttpInfo(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return getCustomerRequestCreation(customerId).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getCustomerWithResponseSpec(UUID customerId) throws WebClientResponseException {
        return getCustomerRequestCreation(customerId);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Customer&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getCustomersRequestCreation() throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Customer&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<Customer> getCustomers() throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return getCustomersRequestCreation().bodyToFlux(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;List&lt;Customer&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<Customer>>> getCustomersWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return getCustomersRequestCreation().toEntityList(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getCustomersWithResponseSpec() throws WebClientResponseException {
        return getCustomersRequestCreation();
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @param customerUpdateRequest The customerUpdateRequest parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec patchCustomerRequestCreation(UUID customerId, CustomerUpdateRequest customerUpdateRequest) throws WebClientResponseException {
        Object postBody = customerUpdateRequest;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling patchCustomer", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerUpdateRequest' is set
        if (customerUpdateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerUpdateRequest' when calling patchCustomer", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("customerId", customerId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}", HttpMethod.PATCH, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @param customerUpdateRequest The customerUpdateRequest parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Customer> patchCustomer(UUID customerId, CustomerUpdateRequest customerUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return patchCustomerRequestCreation(customerId, customerUpdateRequest).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @param customerUpdateRequest The customerUpdateRequest parameter
     * @return ResponseEntity&lt;Customer&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Customer>> patchCustomerWithHttpInfo(UUID customerId, CustomerUpdateRequest customerUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return patchCustomerRequestCreation(customerId, customerUpdateRequest).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param customerId The customerId parameter
     * @param customerUpdateRequest The customerUpdateRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec patchCustomerWithResponseSpec(UUID customerId, CustomerUpdateRequest customerUpdateRequest) throws WebClientResponseException {
        return patchCustomerRequestCreation(customerId, customerUpdateRequest);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerCreateRequest The customerCreateRequest parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postCustomerRequestCreation(CustomerCreateRequest customerCreateRequest) throws WebClientResponseException {
        Object postBody = customerCreateRequest;
        // verify the required parameter 'customerCreateRequest' is set
        if (customerCreateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerCreateRequest' when calling postCustomer", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerCreateRequest The customerCreateRequest parameter
     * @return Customer
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Customer> postCustomer(CustomerCreateRequest customerCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return postCustomerRequestCreation(customerCreateRequest).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerCreateRequest The customerCreateRequest parameter
     * @return ResponseEntity&lt;Customer&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Customer>> postCustomerWithHttpInfo(CustomerCreateRequest customerCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<Customer>() {};
        return postCustomerRequestCreation(customerCreateRequest).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerCreateRequest The customerCreateRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec postCustomerWithResponseSpec(CustomerCreateRequest customerCreateRequest) throws WebClientResponseException {
        return postCustomerRequestCreation(customerCreateRequest);
    }
}
