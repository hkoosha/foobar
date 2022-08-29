# SellerApi

All URIs are relative to *http://localhost:4045*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteSeller**](SellerApi.md#deleteSeller) | **DELETE** /foobar/seller/v1/sellers/{sellerId} |  |
| [**getSeller**](SellerApi.md#getSeller) | **GET** /foobar/seller/v1/sellers/{sellerId} |  |
| [**getSellers**](SellerApi.md#getSellers) | **GET** /foobar/seller/v1/sellers |  |
| [**patchSeller**](SellerApi.md#patchSeller) | **PATCH** /foobar/seller/v1/sellers/{sellerId} |  |
| [**postSeller**](SellerApi.md#postSeller) | **POST** /foobar/seller/v1/sellers |  |



## deleteSeller

> deleteSeller(sellerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.seller.rx.generated.ApiClient;
import io.koosha.foobar.connect.seller.rx.generated.ApiException;
import io.koosha.foobar.connect.seller.rx.generated.Configuration;
import io.koosha.foobar.connect.seller.rx.generated.models.*;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4045");

        SellerApi apiInstance = new SellerApi(defaultClient);
        UUID sellerId = UUID.randomUUID(); // UUID | 
        try {
            apiInstance.deleteSeller(sellerId);
        } catch (ApiException e) {
            System.err.println("Exception when calling SellerApi#deleteSeller");
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


## getSeller

> Seller getSeller(sellerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.seller.rx.generated.ApiClient;
import io.koosha.foobar.connect.seller.rx.generated.ApiException;
import io.koosha.foobar.connect.seller.rx.generated.Configuration;
import io.koosha.foobar.connect.seller.rx.generated.models.*;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4045");

        SellerApi apiInstance = new SellerApi(defaultClient);
        UUID sellerId = UUID.randomUUID(); // UUID | 
        try {
            Seller result = apiInstance.getSeller(sellerId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SellerApi#getSeller");
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
| **sellerId** | **UUID**|  | |

### Return type

[**Seller**](Seller.md)

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


## getSellers

> List&lt;Seller&gt; getSellers()



### Example

```java
// Import classes:
import io.koosha.foobar.connect.seller.rx.generated.ApiClient;
import io.koosha.foobar.connect.seller.rx.generated.ApiException;
import io.koosha.foobar.connect.seller.rx.generated.Configuration;
import io.koosha.foobar.connect.seller.rx.generated.models.*;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4045");

        SellerApi apiInstance = new SellerApi(defaultClient);
        try {
            List<Seller> result = apiInstance.getSellers();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SellerApi#getSellers");
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

[**List&lt;Seller&gt;**](Seller.md)

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


## patchSeller

> Seller patchSeller(sellerId, sellerUpdateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.seller.rx.generated.ApiClient;
import io.koosha.foobar.connect.seller.rx.generated.ApiException;
import io.koosha.foobar.connect.seller.rx.generated.Configuration;
import io.koosha.foobar.connect.seller.rx.generated.models.*;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4045");

        SellerApi apiInstance = new SellerApi(defaultClient);
        UUID sellerId = UUID.randomUUID(); // UUID | 
        SellerUpdateRequest sellerUpdateRequest = new SellerUpdateRequest(); // SellerUpdateRequest | 
        try {
            Seller result = apiInstance.patchSeller(sellerId, sellerUpdateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SellerApi#patchSeller");
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
| **sellerId** | **UUID**|  | |
| **sellerUpdateRequest** | [**SellerUpdateRequest**](SellerUpdateRequest.md)|  | |

### Return type

[**Seller**](Seller.md)

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


## postSeller

> Seller postSeller(sellerCreateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.seller.rx.generated.ApiClient;
import io.koosha.foobar.connect.seller.rx.generated.ApiException;
import io.koosha.foobar.connect.seller.rx.generated.Configuration;
import io.koosha.foobar.connect.seller.rx.generated.models.*;
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4045");

        SellerApi apiInstance = new SellerApi(defaultClient);
        SellerCreateRequest sellerCreateRequest = new SellerCreateRequest(); // SellerCreateRequest | 
        try {
            Seller result = apiInstance.postSeller(sellerCreateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling SellerApi#postSeller");
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
| **sellerCreateRequest** | [**SellerCreateRequest**](SellerCreateRequest.md)|  | |

### Return type

[**Seller**](Seller.md)

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

