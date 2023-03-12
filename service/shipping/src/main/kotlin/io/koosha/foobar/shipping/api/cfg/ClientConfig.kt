package io.koosha.foobar.shipping.api.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.shipping.api.cfg.prop.ServicesProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.customer.generated.ApiClient as Customer_ApiClient
import io.koosha.foobar.connect.marketplace.generated.ApiClient as Marketplace_ApiClient
import io.koosha.foobar.connect.seller.generated.ApiClient as Seller_ApiClient


@EnableFeignClients(basePackages = [PACKAGE])
@EnableRetry
@Configuration
class ClientConfig {

    @Bean
    fun customerAddressClient(
        // beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): AddressApi {

        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.customer().address()
        // apiClient.feignBuilder = SleuthFeignBuilder
        //     .builder(beanFactory)
        //     .decoder(Customer_ApiResponseDecoder(om))

        var api = apiClient.buildClient(AddressApi::class.java)

        if (services.customer().retry)
            api = AddressApi.Retry(api)

        if (services.customer().limit)
            api = AddressApi.Limit(api)

        return api
    }

    @Bean
    fun sellerClient(
        // beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): SellerApi {

        val apiClient = Seller_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.seller().address()
        // apiClient.feignBuilder = SleuthFeignBuilder
        //     .builder(beanFactory)
        //     .decoder(Seller_ApiResponseDecoder(om))

        var api = apiClient.buildClient(SellerApi::class.java)

        if (services.seller().retry)
            api = SellerApi.Retry(api)

        if (services.seller().limit)
            api = SellerApi.Limit(api)

        return api
    }

    @Bean
    fun marketplaceOrderRequestClient(
        // beanFactory: BeanFactory,
        om: ObjectMapper,
        services: ServicesProperties,
    ): OrderRequestApi {

        val apiClient = Marketplace_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = services.marketplace().address()
        // apiClient.feignBuilder = SleuthFeignBuilder
        //     .builder(beanFactory)
        //     .decoder(Marketplace_ApiResponseDecoder(om))

        var api = apiClient.buildClient(OrderRequestApi::class.java)

        if (services.marketplace().retry)
            api = OrderRequestApi.Retry(api)

        if (services.marketplace().limit)
            api = OrderRequestApi.Limit(api)

        return api
    }

}
