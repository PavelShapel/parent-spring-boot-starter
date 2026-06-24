package com.pavelshapel.starter.boot.spring.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
final class CommonApiStarterAutoConfiguration {
  private static final Logger log =
      LoggerFactory.getLogger(CommonApiStarterAutoConfiguration.class);

  CommonApiStarterAutoConfiguration() {
    log.info("common-api-spring-boot-starter was applied ✅");
  }
}
