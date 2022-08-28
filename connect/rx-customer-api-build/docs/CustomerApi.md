# CustomerApi

All URIs are relative to *http://localhost:4043*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteCustomer**](CustomerApi.md#deleteCustomer) | **DELETE** /foobar/customer/v1/customers/{customerId} |  |
| [**getCustomer**](CustomerApi.md#getCustomer) | **GET** /foobar/customer/v1/customers/{customerId} |  |
| [**getCustomers**](CustomerApi.md#getCustomers) | **GET** /foobar/customer/v1/customers |  |
| [**patchCustomer**](CustomerApi.md#patchCustomer) | **PATCH** /foobar/customer/v1/customers/{customerId} |  |
| [**postCustomer**](CustomerApi.md#postCustomer) | **POST** /foobar/customer/v1/customers |  |



## deleteCustomer

> deleteCustomer(customerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.rx.generated.ApiClient;
import io.koosha.foobar.connect.customer.rx.generated.ApiException;
import io.koosha.foobar.connect.customer.rx.generated.Configuration;
import io.koosha.foobar.connect.customer.rx.generated.models.*;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        CustomerApi apiInstance = new CustomerApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        try {
            apiInstance.deleteCustomer(customerId);
        } catch (ApiException e) {
            System.err.println("Exception when calling CustomerApi#deleteCustomer");
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


## getCustomer

> Customer getCustomer(customerId)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.rx.generated.ApiClient;
import io.koosha.foobar.connect.customer.rx.generated.ApiException;
import io.koosha.foobar.connect.customer.rx.generated.Configuration;
import io.koosha.foobar.connect.customer.rx.generated.models.*;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        CustomerApi apiInstance = new CustomerApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        try {
            Customer result = apiInstance.getCustomer(customerId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CustomerApi#getCustomer");
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

[**Customer**](Customer.md)

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


## getCustomers

> List&lt;Customer&gt; getCustomers()



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.rx.generated.ApiClient;
import io.koosha.foobar.connect.customer.rx.generated.ApiException;
import io.koosha.foobar.connect.customer.rx.generated.Configuration;
import io.koosha.foobar.connect.customer.rx.generated.models.*;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        CustomerApi apiInstance = new CustomerApi(defaultClient);
        try {
            List<Customer> result = apiInstance.getCustomers();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CustomerApi#getCustomers");
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

[**List&lt;Customer&gt;**](Customer.md)

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


## patchCustomer

> Customer patchCustomer(customerId, customerUpdateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.rx.generated.ApiClient;
import io.koosha.foobar.connect.customer.rx.generated.ApiException;
import io.koosha.foobar.connect.customer.rx.generated.Configuration;
import io.koosha.foobar.connect.customer.rx.generated.models.*;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        CustomerApi apiInstance = new CustomerApi(defaultClient);
        UUID customerId = UUID.randomUUID(); // UUID | 
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(); // CustomerUpdateRequest | 
        try {
            Customer result = apiInstance.patchCustomer(customerId, customerUpdateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CustomerApi#patchCustomer");
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
| **customerUpdateRequest** | [**CustomerUpdateRequest**](CustomerUpdateRequest.md)|  | |

### Return type

[**Customer**](Customer.md)

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


## postCustomer

> Customer postCustomer(customerCreateRequest)



### Example

```java
// Import classes:
import io.koosha.foobar.connect.customer.rx.generated.ApiClient;
import io.koosha.foobar.connect.customer.rx.generated.ApiException;
import io.koosha.foobar.connect.customer.rx.generated.Configuration;
import io.koosha.foobar.connect.customer.rx.generated.models.*;
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:4043");

        CustomerApi apiInstance = new CustomerApi(defaultClient);
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(); // CustomerCreateRequest | 
        try {
            Customer result = apiInstance.postCustomer(customerCreateRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CustomerApi#postCustomer");
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
| **customerCreateRequest** | [**CustomerCreateRequest**](CustomerCreateRequest.md)|  | |

### Return type

[**Customer**](Customer.md)

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

