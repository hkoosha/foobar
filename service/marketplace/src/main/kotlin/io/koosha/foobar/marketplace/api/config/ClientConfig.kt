package io.koosha.foobar.marketplace.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.connect.customer.rx.generated.api.CustomerApi
import io.koosha.foobar.connect.seller.rx.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.rx.generated.api.ProductApi
import io.koosha.foobar.marketplace.api.config.prop.ServicesProperties
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.customer.rx.generated.ApiClient as Customer_ApiClient
import io.koosha.foobar.connect.seller.rx.generated.ApiClient as Seller_ApiClient
import io.koosha.foobar.connect.warehouse.rx.generated.ApiClient as Warehouse_ApiClient


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

        val apiClient = Customer_ApiClient(om, Customer_ApiClient.createDefaultDateFormat())
        apiClient.basePath = services.customer().address()
        return CustomerApi(apiClient)
    }

    @Bean
    fun sellerClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): SellerApi {

        val apiClient = Seller_ApiClient(om, Seller_ApiClient.createDefaultDateFormat())
        apiClient.basePath = services.seller().address()
        return SellerApi(apiClient)
    }

    @Bean
    fun productClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): ProductApi {

        val apiClient = Warehouse_ApiClient(om, Warehouse_ApiClient.createDefaultDateFormat())
        apiClient.basePath = services.warehouse().address()
        return ProductApi(apiClient)
    }

}
