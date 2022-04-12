package io.koosha.foobar.maker.api.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestApi
import io.koosha.foobar.connect.marketplace.generated.api.OrderRequestLineItemApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.maker.api.cfg.prop.Ports
import io.koosha.foobar.maker.api.cfg.prop.URLs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.koosha.foobar.connect.customer.generated.ApiClient as Customer_ApiClient
import io.koosha.foobar.connect.marketplace.generated.ApiClient as Marketplace_ApiClient
import io.koosha.foobar.connect.seller.generated.ApiClient as Seller_ApiClient
import io.koosha.foobar.connect.warehouse.generated.ApiClient as Warehouse_ApiClient


@Configuration
class ApiConfig(
    private val ports: Ports,
    private val urls: URLs,
) {

    @Bean
    fun customerClient(om: ObjectMapper): CustomerApi {
        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.customer() + ":" + this.ports.customer
        val api = apiClient.buildClient(CustomerApi::class.java)
        return api
    }

    @Bean
    fun addressClient(om: ObjectMapper): AddressApi {
        val apiClient = Customer_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.customer() + ":" + this.ports.customer
        val api = apiClient.buildClient(AddressApi::class.java)
        return api
    }

    @Bean
    fun sellerClient(om: ObjectMapper): SellerApi {
        val apiClient = Seller_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.seller() + ":" + this.ports.seller
        val api = apiClient.buildClient(SellerApi::class.java)
        return api
    }

    @Bean
    fun productClient(om: ObjectMapper): ProductApi {
        val apiClient = Warehouse_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.warehouse() + ":" + this.ports.warehouse
        val api = apiClient.buildClient(ProductApi::class.java)
        return api
    }

    @Bean
    fun availabilityClient(om: ObjectMapper): AvailabilityApi {
        val apiClient = Warehouse_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.warehouse() + ":" + this.ports.warehouse
        val api = apiClient.buildClient(AvailabilityApi::class.java)
        return api
    }

    @Bean
    fun lineItemsClient(om: ObjectMapper): OrderRequestLineItemApi {
        val apiClient = Marketplace_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.marketplace() + ":" + this.ports.marketplace
        val api = apiClient.buildClient(OrderRequestLineItemApi::class.java)
        return api
    }

    @Bean
    fun orderRequestClient(om: ObjectMapper): OrderRequestApi {
        val apiClient = Marketplace_ApiClient()
        apiClient.objectMapper = om
        apiClient.basePath = this.urls.marketplace() + ":" + this.ports.marketplace
        val api = apiClient.buildClient(OrderRequestApi::class.java)
        return api
    }

}
