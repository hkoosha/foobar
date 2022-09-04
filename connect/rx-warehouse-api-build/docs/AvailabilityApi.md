# AvailabilityApi

All URIs are relative to *http://localhost:4046*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteAvailability**](AvailabilityApi.md#deleteAvailability) | **DELETE** /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId} |  |
| [**getAvailabilities**](AvailabilityApi.md#getAvailabilities) | **GET** /foobar/warehouse/v1/products/{productId}/availabilities |  |
| [**getAvailability**](AvailabilityApi.md#getAvailability) | **GET** /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId} |  |
| [**patchAvailability**](AvailabilityApi.md#patchAvailability) | **PATCH** /foobar/warehouse/v1/products/{productId}/availabilities/{sellerId} |  |
| [**postAvailability**](AvailabilityApi.md#postAvailability) | **POST** /foobar/warehouse/v1/products/{productId}/availabilities |  |



## deleteAvailability

> deleteAvailability(productId, sellerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4046");

        AvailabilityApi apiInstance = new AvailabilityApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        UUID sellerId = UUID.randomUUID(); // UUID | 
        try {
            apiInstance.deleteAvailability(productId, sellerId);
        } catch (ApiException e) {
            System.err.println("Exception when calling AvailabilityApi#deleteAvailability");
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
| **sellerId** | **UUID**|  | |

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


## getAvailabilities

> List&lt;Availability&gt; getAvailabilities(productId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4046");

        AvailabilityApi apiInstance = new AvailabilityApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        try {
            List<Availability> result = apiInstance.getAvailabilities(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AvailabilityApi#getAvailabilities");
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

[**List&lt;Availability&gt;**](Availability.md)

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


## getAvailability

> Availability getAvailability(productId, sellerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4046");

        AvailabilityApi apiInstance = new AvailabilityApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        UUID sellerId = UUID.randomUUID(); // UUID | 
        try {
            Availability result = apiInstance.getAvailability(productId, sellerId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AvailabilityApi#getAvailability");
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
| **sellerId** | **UUID**|  | |

### Return type

[**Availability**](Availability.md)

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


## patchAvailability

> Availability patchAvailability(productId, sellerId, availabilityUpdateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4046");

        AvailabilityApi apiInstance = new AvailabilityApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        UUID sellerId = UUID.randomUUID(); // UUID | 
        AvailabilityUpdateRequest availabilityUpdateRequest = new AvailabilityUpdateRequest(); // AvailabilityUpdateRequest | 
        try {
            Availability result = apiInstance.patchAvailability(productId, sellerId, availabilityUpdateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AvailabilityApi#patchAvailability");
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
| **sellerId** | **UUID**|  | |
| **availabilityUpdateRequest** | [**AvailabilityUpdateRequest**](AvailabilityUpdateRequest.md)|  | |

### Return type

[**Availability**](Availability.md)

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


## postAvailability

> Availability postAvailability(productId, availabilityCreateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient;
import io.koosha.foobar.connect.warehouse.rx.generated.ApiException;
import io.koosha.foobar.connect.warehouse.rx.generated.Configuration;
import io.koosha.foobar.connect.warehouse.rx.generated.models.*;
import io.koosha.foobar.connect.warehouse.rx.generated.api.AvailabilityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4046");

        AvailabilityApi apiInstance = new AvailabilityApi(defaultClient);
        UUID productId = UUID.randomUUID(); // UUID | 
        AvailabilityCreateRequest availabilityCreateRequest = new AvailabilityCreateRequest(); // AvailabilityCreateRequest | 
        try {
            Availability result = apiInstance.postAvailability(productId, availabilityCreateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AvailabilityApi#postAvailability");
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
| **availabilityCreateRequest** | [**AvailabilityCreateRequest**](AvailabilityCreateRequest.md)|  | |

### Return type

[**Availability**](Availability.md)

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

