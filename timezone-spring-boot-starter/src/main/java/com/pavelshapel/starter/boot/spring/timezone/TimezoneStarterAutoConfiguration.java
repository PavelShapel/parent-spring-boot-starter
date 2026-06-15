package com.pavelshapel.starter.boot.spring.timezone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
final class TimezoneStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(TimezoneStarterAutoConfiguration.class);

  TimezoneStarterAutoConfiguration() {
    log.info("timezone-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  TimezoneRegistry timezoneRegistry() {
    return new TimezoneRegistry();
  }
}
