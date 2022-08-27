package io.koosha.foobar.marketplace.api.config

import io.koosha.foobar.common.PACKAGE
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@Configuration
@EnableR2dbcRepositories(basePackages = [PACKAGE])
@EnableR2dbcAuditing
class R2dbcConfig {

    @Bean
    fun flywayStrategy() = FlywayMigrationStrategy { it.migrate() }

}
