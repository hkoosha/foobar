# AddressApi

All URIs are relative to *http://localhost:4043*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteAddress**](AddressApi.md#deleteAddress) | **DELETE** /foobar/customer/v1/customers/{customerId}/addresses/{addressId} |  |
| [**getAddress**](AddressApi.md#getAddress) | **GET** /foobar/customer/v1/customers/{customerId}/addresses/{addressId} |  |
| [**getAddresses**](AddressApi.md#getAddresses) | **GET** /foobar/customer/v1/customers/{customerId}/addresses |  |
| [**postAddress**](AddressApi.md#postAddress) | **POST** /foobar/customer/v1/customers/{customerId}/addresses |  |



## deleteAddress

> deleteAddress(customerId, addressId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.ApiException;
import io.koosha.foobar.connect.customer.generated.Configuration;
import io.koosha.foobar.connect.customer.generated.models.*;
import io.koosha.foobar.connect.customer.generated.api.AddressApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        AddressApi apiInstance = new AddressApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        Long addressId = 56L; // Long | 
        try {
            apiInstance.deleteAddress(customerId, addressId);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressApi#deleteAddress");
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
| **customerId** | **UUID**|  | |
| **addressId** | **Long**|  | |

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


## getAddress

> Address getAddress(customerId, addressId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.ApiException;
import io.koosha.foobar.connect.customer.generated.Configuration;
import io.koosha.foobar.connect.customer.generated.models.*;
import io.koosha.foobar.connect.customer.generated.api.AddressApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        AddressApi apiInstance = new AddressApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        Long addressId = 56L; // Long | 
        try {
            Address result = apiInstance.getAddress(customerId, addressId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressApi#getAddress");
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
| **customerId** | **UUID**|  | |
| **addressId** | **Long**|  | |

### Return type

[**Address**](Address.md)

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


## getAddresses

> List&lt;Address&gt; getAddresses(customerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.ApiException;
import io.koosha.foobar.connect.customer.generated.Configuration;
import io.koosha.foobar.connect.customer.generated.models.*;
import io.koosha.foobar.connect.customer.generated.api.AddressApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        AddressApi apiInstance = new AddressApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        try {
            List<Address> result = apiInstance.getAddresses(customerId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressApi#getAddresses");
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
| **customerId** | **UUID**|  | |

### Return type

[**List&lt;Address&gt;**](Address.md)

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


## postAddress

> Address postAddress(customerId, customerAddressCreateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.generated.ApiClient;
import io.koosha.foobar.connect.customer.generated.ApiException;
import io.koosha.foobar.connect.customer.generated.Configuration;
import io.koosha.foobar.connect.customer.generated.models.*;
import io.koosha.foobar.connect.customer.generated.api.AddressApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        AddressApi apiInstance = new AddressApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest(); // CustomerAddressCreateRequest | 
        try {
            Address result = apiInstance.postAddress(customerId, customerAddressCreateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressApi#postAddress");
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
| **customerId** | **UUID**|  | |
| **customerAddressCreateRequest** | [**CustomerAddressCreateRequest**](CustomerAddressCreateRequest.md)|  | |

### Return type

[**Address**](Address.md)

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

