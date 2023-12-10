package io.koosha.foobar.marketplace.api.cfg

import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.common.PROFILE__DISABLE_DB
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@Configuration(proxyBeanMethods = false)
@EnableR2dbcRepositories(basePackages = [PACKAGE])
@EnableR2dbcAuditing
class R2dbcConfig {

    @Profile("!$PROFILE__DISABLE_DB")
    @Bean
    fun flywayStrategyOnDb() = FlywayMigrationStrategy { it.migrate() }

    @Profile(PROFILE__DISABLE_DB)
    @Bean
    fun flywayStrategyNoDb() = FlywayMigrationStrategy { }

}
