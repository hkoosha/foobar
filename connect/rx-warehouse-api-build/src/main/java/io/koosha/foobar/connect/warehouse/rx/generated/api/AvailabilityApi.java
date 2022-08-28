package io.koosha.foobar.connect.warehouse.rx.generated.api;

import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;

import io.koosha.foobar.connect.warehouse.rx.generated.api.Availability;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityCreateRequest;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityUpdateRequest;
import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityNotFoundApiError;
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
public class AvailabilityApi {
    private ApiClient apiClient;

    public AvailabilityApi() {
        this(new ApiClient());
    }

    @Autowired
    public AvailabilityApi(ApiClient apiClient) {
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
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteAvailabilityRequestCreation(UUID productId, UUID sellerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling deleteAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling deleteAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);
        pathParams.put("sellerId", sellerId);

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
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteAvailability(UUID productId, UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteAvailabilityRequestCreation(productId, sellerId).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Void>> deleteAvailabilityWithHttpInfo(UUID productId, UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteAvailabilityRequestCreation(productId, sellerId).toEntity(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return List&lt;Availability&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAvailabilitiesRequestCreation(UUID productId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling getAvailabilities", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);

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

        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}/availabilities", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return List&lt;Availability&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<Availability> getAvailabilities(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return getAvailabilitiesRequestCreation(productId).bodyToFlux(localVarReturnType);
    }

    public Mono<ResponseEntity<List<Availability>>> getAvailabilitiesWithHttpInfo(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return getAvailabilitiesRequestCreation(productId).toEntityList(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAvailabilityRequestCreation(UUID productId, UUID sellerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling getAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling getAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);
        pathParams.put("sellerId", sellerId);

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

        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Availability> getAvailability(UUID productId, UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return getAvailabilityRequestCreation(productId, sellerId).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Availability>> getAvailabilityWithHttpInfo(UUID productId, UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return getAvailabilityRequestCreation(productId, sellerId).toEntity(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @param availabilityUpdateRequest The availabilityUpdateRequest parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec patchAvailabilityRequestCreation(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) throws WebClientResponseException {
        Object postBody = availabilityUpdateRequest;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling patchAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling patchAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'availabilityUpdateRequest' is set
        if (availabilityUpdateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'availabilityUpdateRequest' when calling patchAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);
        pathParams.put("sellerId", sellerId);

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

        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}/availabilities/{sellerId}", HttpMethod.PATCH, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @param sellerId The sellerId parameter
     * @param availabilityUpdateRequest The availabilityUpdateRequest parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Availability> patchAvailability(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return patchAvailabilityRequestCreation(productId, sellerId, availabilityUpdateRequest).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Availability>> patchAvailabilityWithHttpInfo(UUID productId, UUID sellerId, AvailabilityUpdateRequest availabilityUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return patchAvailabilityRequestCreation(productId, sellerId, availabilityUpdateRequest).toEntity(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param productId The productId parameter
     * @param availabilityCreateRequest The availabilityCreateRequest parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postAvailabilityRequestCreation(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) throws WebClientResponseException {
        Object postBody = availabilityCreateRequest;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling postAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'availabilityCreateRequest' is set
        if (availabilityCreateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'availabilityCreateRequest' when calling postAvailability", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);

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

        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}/availabilities", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param productId The productId parameter
     * @param availabilityCreateRequest The availabilityCreateRequest parameter
     * @return Availability
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Availability> postAvailability(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return postAvailabilityRequestCreation(productId, availabilityCreateRequest).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Availability>> postAvailabilityWithHttpInfo(UUID productId, AvailabilityCreateRequest availabilityCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Availability> localVarReturnType = new ParameterizedTypeReference<Availability>() {};
        return postAvailabilityRequestCreation(productId, availabilityCreateRequest).toEntity(localVarReturnType);
    }
}
