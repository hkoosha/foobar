package io.koosha.foobar.shipping.api.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.shipping.api.cfg.prop.ServiceAddress
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.sleuth.instrument.web.client.feign.SleuthFeignBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.customer.generated.ApiClient as Customer_ApiClient
import io.koosha.foobar.connect.customer.generated.ApiResponseDecoder as Customer_ApiResponseDecoder
import io.koosha.foobar.connect.marketplace.generated.ApiClient as Marketplace_ApiClient
import io.koosha.foobar.connect.marketplace.generated.ApiResponseDecoder as Marketplace_ApiResponseDecoder
import io.koosha.foobar.connect.seller.generated.ApiClient as Seller_ApiClient
import io.koosha.foobar.connect.seller.generated.ApiResponseDecoder as Seller_ApiResponseDecoder


@EnableFeignClients(basePackages = [PACKAGE])
@EnableRetry
@Configuration
class ClientConfig {

    @Bean
    fun customerAddressClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        serviceAddress: ServiceAddress,
    ): AddressApi {

        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.customerAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Customer_ApiResponseDecoder(om))

        val api = apiClient.buildClient(AddressApi::class.java)

        val retry = AddressApi.Retry(api)

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
    fun orderRequestClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        serviceAddress: ServiceAddress,
    ): OrderRequestApi {

        val apiClient = Marketplace_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.marketplaceAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Marketplace_ApiResponseDecoder(om))

        val api = apiClient.buildClient(OrderRequestApi::class.java)

        val retry = OrderRequestApi.Retry(api)

        return retry
    }

}
