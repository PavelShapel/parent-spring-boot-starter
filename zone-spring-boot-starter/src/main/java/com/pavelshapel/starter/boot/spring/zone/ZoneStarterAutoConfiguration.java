package com.pavelshapel.starter.boot.spring.zone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
final class ZoneStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(ZoneStarterAutoConfiguration.class);

  ZoneStarterAutoConfiguration() {
    log.info("zone-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  ZoneRegistry zoneRegistry() {
    return new ZoneRegistry();
  }
}
