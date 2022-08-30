package io.koosha.foobar.loader

import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.loader.api.service.entitygen.dependent.AvailabilityGeneratorService
import io.koosha.foobar.loader.api.service.entitygen.dependent.LineItemGeneratorService
import io.koosha.foobar.loader.api.service.entitygen.dependent.LineItemUpdaterService
import io.koosha.foobar.loader.api.service.entitygen.dependent.OrderRequestGeneratorService
import io.koosha.foobar.loader.api.service.entitygen.root.CustomerGeneratorService
import io.koosha.foobar.loader.api.service.entitygen.root.ProductGeneratorService
import io.koosha.foobar.loader.api.service.entitygen.root.SellerGeneratorService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.util.concurrent.Executors
import java.util.concurrent.Future


@SpringBootApplication(scanBasePackages = [PACKAGE])
@ConfigurationPropertiesScan
class LoaderApplication


fun main(vararg args: String) {

    val ctx = runApplication<LoaderApplication>(*args)

    val exec = Executors.newCachedThreadPool()
    val futures = mutableListOf<Future<*>>()

    val customerGenerator = ctx.getBean(CustomerGeneratorService::class.java)
    val productGenerator = ctx.getBean(ProductGeneratorService::class.java)
    val sellerGenerator = ctx.getBean(SellerGeneratorService::class.java)

    futures += exec.submit { customerGenerator.generate { true } }
    futures += exec.submit { productGenerator.generate { true } }
    futures += exec.submit { sellerGenerator.generate { true } }

    Thread.sleep(300)

    val availabilityGenerator = ctx.getBean(AvailabilityGeneratorService::class.java)
    val orderRequestGenerator = ctx.getBean(OrderRequestGeneratorService::class.java)
    val lineItemGenerator = ctx.getBean(LineItemGeneratorService::class.java)
    val lineItemUpdater = ctx.getBean(LineItemUpdaterService::class.java)

    futures += exec.submit { availabilityGenerator.generate { true } }
    futures += exec.submit { orderRequestGenerator.generate { true } }
    futures += exec.submit { lineItemGenerator.generate { true } }
    futures += exec.submit { lineItemUpdater.generate { true } }

    for (future in futures)
        future.get()
}
