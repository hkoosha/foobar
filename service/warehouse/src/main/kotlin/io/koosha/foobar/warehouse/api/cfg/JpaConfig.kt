package io.koosha.foobar.warehouse.api.cfg

import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.common.PROFILE__DISABLE_DB
import io.koosha.foobar.common.PROFILE__TEST
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(value = [PACKAGE])
@Profile("!$PROFILE__TEST&&!$PROFILE__DISABLE_DB")
class JpaConfig
