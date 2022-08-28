package io.koosha.foobar.connect.warehouse.rx.generated.api;

import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;

import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityBadValueApiError;
import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityIllegalStateApiError;
import io.koosha.foobar.connect.warehouse.rx.generated.api.EntityNotFoundApiError;
import io.koosha.foobar.connect.warehouse.rx.generated.api.Product;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductCreateRequest;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductUpdateRequest;
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
public class ProductApi {
    private ApiClient apiClient;

    public ProductApi() {
        this(new ApiClient());
    }

    @Autowired
    public ProductApi(ApiClient apiClient) {
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
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteProductRequestCreation(UUID productId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling deleteProduct", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteProduct(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteProductRequestCreation(productId).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Void>> deleteProductWithHttpInfo(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return deleteProductRequestCreation(productId).toEntity(localVarReturnType);
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
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getProductRequestCreation(UUID productId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling getProduct", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Product> getProduct(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return getProductRequestCreation(productId).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Product>> getProductWithHttpInfo(UUID productId) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return getProductRequestCreation(productId).toEntity(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Product&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getProductsRequestCreation() throws WebClientResponseException {
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

        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>200</b> - OK
     * @return List&lt;Product&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<Product> getProducts() throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return getProductsRequestCreation().bodyToFlux(localVarReturnType);
    }

    public Mono<ResponseEntity<List<Product>>> getProductsWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return getProductsRequestCreation().toEntityList(localVarReturnType);
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
     * @param productUpdateRequest The productUpdateRequest parameter
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec patchProductRequestCreation(UUID productId, ProductUpdateRequest productUpdateRequest) throws WebClientResponseException {
        Object postBody = productUpdateRequest;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling patchProduct", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'productUpdateRequest' is set
        if (productUpdateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'productUpdateRequest' when calling patchProduct", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products/{productId}", HttpMethod.PATCH, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
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
     * @param productUpdateRequest The productUpdateRequest parameter
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Product> patchProduct(UUID productId, ProductUpdateRequest productUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return patchProductRequestCreation(productId, productUpdateRequest).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Product>> patchProductWithHttpInfo(UUID productId, ProductUpdateRequest productUpdateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return patchProductRequestCreation(productId, productUpdateRequest).toEntity(localVarReturnType);
    }
    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param productCreateRequest The productCreateRequest parameter
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postProductRequestCreation(ProductCreateRequest productCreateRequest) throws WebClientResponseException {
        Object postBody = productCreateRequest;
        // verify the required parameter 'productCreateRequest' is set
        if (productCreateRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'productCreateRequest' when calling postProduct", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return apiClient.invokeAPI("/foobar/warehouse/v1/products", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>404</b> - Not Found
     * <p><b>400</b> - Bad Request
     * <p><b>403</b> - Forbidden
     * <p><b>503</b> - Service Unavailable
     * <p><b>201</b> - Created
     * @param productCreateRequest The productCreateRequest parameter
     * @return Product
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Product> postProduct(ProductCreateRequest productCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return postProductRequestCreation(productCreateRequest).bodyToMono(localVarReturnType);
    }

    public Mono<ResponseEntity<Product>> postProductWithHttpInfo(ProductCreateRequest productCreateRequest) throws WebClientResponseException {
        ParameterizedTypeReference<Product> localVarReturnType = new ParameterizedTypeReference<Product>() {};
        return postProductRequestCreation(productCreateRequest).toEntity(localVarReturnType);
    }
}
