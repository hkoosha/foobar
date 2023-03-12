package io.koosha.foobar.connect.customer.rx.generated.api;

import io.koosha.foobar.connect.customer.rx.generated.ApiClient;

import io.koosha.foobar.connect.customer.rx.generated.api.Address;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerAddressCreateRequest;
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

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AddressApi {
    private ApiClient apiClient;

    public AddressApi() {
        this(new ApiClient());
    }

    @Autowired
    public AddressApi(ApiClient apiClient) {
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
     * @param addressId The addressId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteAddressRequestCreation(UUID customerId, Long addressId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling deleteAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'addressId' is set
        if (addressId == null) {
            throw new WebClientResponseException("Missing the required parameter 'addressId' when calling deleteAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("customerId", customerId);
        pathParams.put("addressId", addressId);

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
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}/addresses/{addressId}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @param addressId The addressId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteAddress(UUID customerId, Long addressId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteAddressRequestCreation(customerId, addressId).bodyToMono(localVarReturnType);
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
     * @param addressId The addressId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> deleteAddressWithHttpInfo(UUID customerId, Long addressId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteAddressRequestCreation(customerId, addressId).toEntity(localVarReturnType);
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
     * @param addressId The addressId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec deleteAddressWithResponseSpec(UUID customerId, Long addressId) throws WebClientResponseException {
        return deleteAddressRequestCreation(customerId, addressId);
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
     * @param addressId The addressId parameter
     * @return Address
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAddressRequestCreation(UUID customerId, Long addressId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling getAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'addressId' is set
        if (addressId == null) {
            throw new WebClientResponseException("Missing the required parameter 'addressId' when calling getAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("customerId", customerId);
        pathParams.put("addressId", addressId);

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

        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}/addresses/{addressId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @param addressId The addressId parameter
     * @return Address
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Address> getAddress(UUID customerId, Long addressId) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return getAddressRequestCreation(customerId, addressId).bodyToMono(localVarReturnType);
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
     * @param addressId The addressId parameter
     * @return ResponseEntity&lt;Address&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Address>> getAddressWithHttpInfo(UUID customerId, Long addressId) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return getAddressRequestCreation(customerId, addressId).toEntity(localVarReturnType);
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
     * @param addressId The addressId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getAddressWithResponseSpec(UUID customerId, Long addressId) throws WebClientResponseException {
        return getAddressRequestCreation(customerId, addressId);
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
     * @return List&lt;Address&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAddressesRequestCreation(UUID customerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling getAddresses", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}/addresses", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @return List&lt;Address&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<Address> getAddresses(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return getAddressesRequestCreation(customerId).bodyToFlux(localVarReturnType);
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
     * @return ResponseEntity&lt;List&lt;Address&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<Address>>> getAddressesWithHttpInfo(UUID customerId) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return getAddressesRequestCreation(customerId).toEntityList(localVarReturnType);
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
    public ResponseSpec getAddressesWithResponseSpec(UUID customerId) throws WebClientResponseException {
        return getAddressesRequestCreation(customerId);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerId The customerId parameter
     * @param customerAddressCreateRequest The customerAddressCreateRequest parameter
     * @return Address
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postAddressRequestCreation(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) throws WebClientResponseException {
        Object postBody = customerAddressCreateRequest;
        // verify the required parameter 'customerId' is set
        if (customerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerId' when calling postAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerAddressCreateRequest' is set
        if (customerAddressCreateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'customerAddressCreateRequest' when calling postAddress", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return apiClient.invokeAPI("/foobar/customer/v1/customers/{customerId}/addresses", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerId The customerId parameter
     * @param customerAddressCreateRequest The customerAddressCreateRequest parameter
     * @return Address
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Address> postAddress(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return postAddressRequestCreation(customerId, customerAddressCreateRequest).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerId The customerId parameter
     * @param customerAddressCreateRequest The customerAddressCreateRequest parameter
     * @return ResponseEntity&lt;Address&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Address>> postAddressWithHttpInfo(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Address> localVarReturnType = new ParameterizedTypeReference<Address>() {};
        return postAddressRequestCreation(customerId, customerAddressCreateRequest).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param customerId The customerId parameter
     * @param customerAddressCreateRequest The customerAddressCreateRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec postAddressWithResponseSpec(UUID customerId, CustomerAddressCreateRequest customerAddressCreateRequest) throws WebClientResponseException {
        return postAddressRequestCreation(customerId, customerAddressCreateRequest);
    }
}
