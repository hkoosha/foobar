# ProductApi

All URIs are relative to *http://localhost:4041*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteProduct**](ProductApi.md#deleteProduct) | **DELETE** /foobar/warehouse/v1/products/{productId} |  |
| [**getProduct**](ProductApi.md#getProduct) | **GET** /foobar/warehouse/v1/products/{productId} |  |
| [**getProducts**](ProductApi.md#getProducts) | **GET** /foobar/warehouse/v1/products |  |
| [**patchProduct**](ProductApi.md#patchProduct) | **PATCH** /foobar/warehouse/v1/products/{productId} |  |
| [**postProduct**](ProductApi.md#postProduct) | **POST** /foobar/warehouse/v1/products |  |



## deleteProduct

> deleteProduct(productId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4041");

        ProductApi apiInstance = new ProductApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        try {
            apiInstance.deleteProduct(productId);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#deleteProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **UUID**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **404** | Not Found |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **503** | Service Unavailable |  -  |
| **204** | No Content |  -  |


## getProduct

> Product getProduct(productId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4041");

        ProductApi apiInstance = new ProductApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        try {
            Product result = apiInstance.getProduct(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#getProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **UUID**|  | |

### Return type

[**Product**](Product.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **404** | Not Found |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **503** | Service Unavailable |  -  |
| **200** | OK |  -  |


## getProducts

> List&lt;Product&gt; getProducts()



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4041");

        ProductApi apiInstance = new ProductApi(defaultClient);
        try {
            List<Product> result = apiInstance.getProducts();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#getProducts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**List&lt;Product&gt;**](Product.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **404** | Not Found |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **503** | Service Unavailable |  -  |
| **200** | OK |  -  |


## patchProduct

> Product patchProduct(productId, productUpdateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4041");

        ProductApi apiInstance = new ProductApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(); // ProductUpdateRequest | 
        try {
            Product result = apiInstance.patchProduct(productId, productUpdateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#patchProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **UUID**|  | |
| **productUpdateRequest** | [**ProductUpdateRequest**](ProductUpdateRequest.md)|  | |

### Return type

[**Product**](Product.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **404** | Not Found |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **503** | Service Unavailable |  -  |
| **200** | OK |  -  |


## postProduct

> Product postProduct(productCreateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4041");

        ProductApi apiInstance = new ProductApi(defaultClient);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(); // ProductCreateRequest | 
        try {
            Product result = apiInstance.postProduct(productCreateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProductApi#postProduct");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productCreateRequest** | [**ProductCreateRequest**](ProductCreateRequest.md)|  | |

### Return type

[**Product**](Product.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **404** | Not Found |  -  |
| **400** | Bad Request |  -  |
| **403** | Forbidden |  -  |
| **503** | Service Unavailable |  -  |
| **201** | Created |  -  |

