package io.koosha.foobar.warehouse.api.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.warehouse.api.cfg.prop.ServiceAddress
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.sleuth.instrument.web.client.feign.SleuthFeignBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import io.koosha.foobar.connect.seller.generated.ApiClient as Seller_ApiClient
import io.koosha.foobar.connect.seller.generated.ApiResponseDecoder as Seller_ApiResponseDecoder


@EnableFeignClients(basePackages = [PACKAGE])
@EnableRetry
@Configuration
class ClientConfig {

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

}
