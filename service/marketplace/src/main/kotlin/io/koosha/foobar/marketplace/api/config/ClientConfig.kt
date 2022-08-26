package io.koosha.foobar.marketplace.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.config.prop.ServicesProperties
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.sleuth.instrument.web.client.feign.SleuthFeignBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.customer.generated.ApiClient as Customer_ApiClient
import io.koosha.foobar.connect.customer.generated.ApiResponseDecoder as Customer_ApiResponseDecoder
import io.koosha.foobar.connect.seller.generated.ApiClient as Seller_ApiClient
import io.koosha.foobar.connect.seller.generated.ApiResponseDecoder as Seller_ApiResponseDecoder
import io.koosha.foobar.connect.warehouse.generated.ApiClient as Warehouse_ApiClient
import io.koosha.foobar.connect.warehouse.generated.ApiResponseDecoder as Warehouse_ApiResponseDecoder


@EnableFeignClients
@EnableRetry
@Configuration
class ClientConfig {

    @Bean
    fun customerClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): CustomerApi {

        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.customer().address()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Customer_ApiResponseDecoder(om))

        var api: CustomerApi = apiClient.buildClient(CustomerApi::class.java)

        if (services.customer().retry)
            api = CustomerApi.Retry(api)

        if (services.customer().limit)
            api = CustomerApi.Limit(api)

        return api
    }

    @Bean
    fun sellerClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): SellerApi {

        val apiClient = Seller_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.seller().address()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Seller_ApiResponseDecoder(om))

        var api = apiClient.buildClient(SellerApi::class.java)

        if (services.seller().retry)
            api = SellerApi.Retry(api)

        if (services.seller().limit)
            api = SellerApi.Limit(api)

        return api
    }

    @Bean
    fun productClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): ProductApi {

        val apiClient = Warehouse_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.warehouse().address()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Warehouse_ApiResponseDecoder(om))

        var api = apiClient.buildClient(ProductApi::class.java)

        if (services.warehouse().retry)
            api = ProductApi.Retry(api)

        if (services.warehouse().limit)
            api = ProductApi.Limit(api)

        return api
    }

}
