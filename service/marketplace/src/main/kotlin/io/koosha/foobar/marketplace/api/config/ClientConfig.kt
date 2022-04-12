package io.koosha.foobar.marketplace.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.config.prop.ServiceAddress
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
        serviceAddress: ServiceAddress,
    ): CustomerApi {

        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.customerAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Customer_ApiResponseDecoder(om))

        val api = apiClient.buildClient(CustomerApi::class.java)

        val retry = CustomerApi.Retry(api)

        return retry
    }

    @Bean
    fun sellerClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        serviceAddress: ServiceAddress,
    ): SellerApi {

        val apiClient = Seller_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.sellerAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Seller_ApiResponseDecoder(om))

        val api = apiClient.buildClient(SellerApi::class.java)

        val retry = SellerApi.Retry(api)

        return retry
    }

    @Bean
    fun productClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        serviceAddress: ServiceAddress,
    ): ProductApi {

        val apiClient = Warehouse_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.warehouseAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Warehouse_ApiResponseDecoder(om))
        val api = apiClient.buildClient(ProductApi::class.java)
        return ProductApi.Retry(api)
    }

}