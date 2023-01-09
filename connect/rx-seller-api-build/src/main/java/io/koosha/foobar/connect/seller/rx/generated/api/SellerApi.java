package io.koosha.foobar.connect.seller.rx.generated.api;

import io.koosha.foobar.connect.seller.rx.generated.ApiClient;

import io.koosha.foobar.connect.seller.rx.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.seller.rx.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.seller.rx.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.seller.rx.generated.api.Seller;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerCreateRequest;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerUpdateRequest;
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
public class SellerApi {
    private ApiClient apiClient;

    public SellerApi() {
        this(new ApiClient());
    }

    @Autowired
    public SellerApi(ApiClient apiClient) {
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
     * @param sellerId The sellerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteSellerRequestCreation(UUID sellerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling deleteSeller", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

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
        return apiClient.invokeAPI("/foobar/seller/v1/sellers/{sellerId}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param sellerId The sellerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteSeller(UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteSellerRequestCreation(sellerId).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param sellerId The sellerId parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> deleteSellerWithHttpInfo(UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteSellerRequestCreation(sellerId).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>204</b> - No Content
     * @param sellerId The sellerId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec deleteSellerWithResponseSpec(UUID sellerId) throws WebClientResponseException {
        return deleteSellerRequestCreation(sellerId);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getSellerRequestCreation(UUID sellerId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling getSeller", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

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

        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return apiClient.invokeAPI("/foobar/seller/v1/sellers/{sellerId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Seller> getSeller(UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return getSellerRequestCreation(sellerId).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @return ResponseEntity&lt;Seller&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Seller>> getSellerWithHttpInfo(UUID sellerId) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return getSellerRequestCreation(sellerId).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getSellerWithResponseSpec(UUID sellerId) throws WebClientResponseException {
        return getSellerRequestCreation(sellerId);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Seller&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getSellersRequestCreation() throws WebClientResponseException {
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

        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return apiClient.invokeAPI("/foobar/seller/v1/sellers", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Seller&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<Seller> getSellers() throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return getSellersRequestCreation().bodyToFlux(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;List&lt;Seller&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<Seller>>> getSellersWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return getSellersRequestCreation().toEntityList(localVarReturnType);
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
    public ResponseSpec getSellersWithResponseSpec() throws WebClientResponseException {
        return getSellersRequestCreation();
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @param sellerUpdateRequest The sellerUpdateRequest parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec patchSellerRequestCreation(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) throws WebClientResponseException {
        Object postBody = sellerUpdateRequest;
        // verify the required parameter 'sellerId' is set
        if (sellerId == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerId' when calling patchSeller", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'sellerUpdateRequest' is set
        if (sellerUpdateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerUpdateRequest' when calling patchSeller", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

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

        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return apiClient.invokeAPI("/foobar/seller/v1/sellers/{sellerId}", HttpMethod.PATCH, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @param sellerUpdateRequest The sellerUpdateRequest parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Seller> patchSeller(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return patchSellerRequestCreation(sellerId, sellerUpdateRequest).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @param sellerUpdateRequest The sellerUpdateRequest parameter
     * @return ResponseEntity&lt;Seller&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Seller>> patchSellerWithHttpInfo(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return patchSellerRequestCreation(sellerId, sellerUpdateRequest).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @param sellerId The sellerId parameter
     * @param sellerUpdateRequest The sellerUpdateRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec patchSellerWithResponseSpec(UUID sellerId, SellerUpdateRequest sellerUpdateRequest) throws WebClientResponseException {
        return patchSellerRequestCreation(sellerId, sellerUpdateRequest);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param sellerCreateRequest The sellerCreateRequest parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postSellerRequestCreation(SellerCreateRequest sellerCreateRequest) throws WebClientResponseException {
        Object postBody = sellerCreateRequest;
        // verify the required parameter 'sellerCreateRequest' is set
        if (sellerCreateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'sellerCreateRequest' when calling postSeller", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return apiClient.invokeAPI("/foobar/seller/v1/sellers", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param sellerCreateRequest The sellerCreateRequest parameter
     * @return Seller
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Seller> postSeller(SellerCreateRequest sellerCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return postSellerRequestCreation(sellerCreateRequest).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param sellerCreateRequest The sellerCreateRequest parameter
     * @return ResponseEntity&lt;Seller&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Seller>> postSellerWithHttpInfo(SellerCreateRequest sellerCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Seller> localVarReturnType = new ParameterizedTypeReference<Seller>() {};
        return postSellerRequestCreation(sellerCreateRequest).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param sellerCreateRequest The sellerCreateRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec postSellerWithResponseSpec(SellerCreateRequest sellerCreateRequest) throws WebClientResponseException {
        return postSellerRequestCreation(sellerCreateRequest);
    }
}
