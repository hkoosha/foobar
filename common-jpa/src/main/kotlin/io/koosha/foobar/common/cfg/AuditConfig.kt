package io.koosha.foobar.common.cfg

import io.koosha.foobar.common.PROFILE__TEST
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@Configuration
@EnableJpaAuditing
@Profile("!$PROFILE__TEST")
class AuditConfig
