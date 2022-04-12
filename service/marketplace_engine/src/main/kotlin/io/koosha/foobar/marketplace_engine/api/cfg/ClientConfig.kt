package io.koosha.foobar.marketplace_engine.api.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItemApi
import io.koosha.foobar.marketplace_engine.api.cfg.prop.ServiceAddress
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.sleuth.instrument.web.client.feign.SleuthFeignBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.marketplace.generated.ApiClient as Marketplace_ApiClient
import io.koosha.foobar.connect.marketplace.generated.ApiResponseDecoder as Marketplace_ApiResponseDecoder


// TODO send information queries using this client in the kafka event and remove these clients from marketplace_engine.
@EnableFeignClients(basePackages = [PACKAGE])
@EnableRetry
@Configuration
class ClientConfig {

    @Bean
    fun lineItemsClient(
        beanFactory: BeanFactory,
        om: ObjectMapper,
        serviceAddress: ServiceAddress,
    ): OrderRequestLineItemApi {

        val apiClient = Marketplace_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = serviceAddress.marketplaceAddress()
        apiClient.feignBuilder = SleuthFeignBuilder
            .builder(beanFactory)
            .decoder(Marketplace_ApiResponseDecoder(om))

        val api = apiClient.buildClient(OrderRequestLineItemApi::class.java)

        val retry = OrderRequestLineItemApi.Retry(api)

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
