package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.common.PROFILE__TEST
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@Configuration
@EnableJpaRepositories(value = [PACKAGE])
@EntityScan(value = [PACKAGE])
@Profile("!$PROFILE__TEST")
class JpaConfig
