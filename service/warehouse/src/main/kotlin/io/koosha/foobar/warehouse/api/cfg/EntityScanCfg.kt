package io.koosha.foobar.warehouse.api.cfg

import io.koosha.foobar.common.PACKAGE
import io.koosha.foobar.common.PROFILE__DISABLE_DB
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EntityScan(value = [PACKAGE])
@Profile("!$PROFILE__DISABLE_DB")
class EntityScanCfg
