package io.koosha.foobar.warehouse.api.cfg

import io.koosha.foobar.common.PACKAGE
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = [PACKAGE])
@Configuration(proxyBeanMethods = false)
class ClientConfig
