package io.koosha.foobar.marketplaceengine.api.cfg

import io.koosha.foobar.common.PROFILE__KAFKA
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Profile(PROFILE__KAFKA)
@Configuration
class MarketplaceEngineKafkaConfig
