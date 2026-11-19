package com.pavelshapel.starter.boot.spring.api.common;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
final class CommonApiStarterAutoConfiguration {
  private static final Logger log =
      LoggerFactory.getLogger(CommonApiStarterAutoConfiguration.class);

  CommonApiStarterAutoConfiguration() {
    log.info("common-api-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  RuntimeEnvironment runtimeEnvironment(Environment environment) {
    String[] activeProfiles = environment.getActiveProfiles();
    Set<String> profiles =
        activeProfiles.length > 0
            ? Set.of(activeProfiles)
            : Set.of(environment.getDefaultProfiles());
    return new RuntimeEnvironment(profiles);
  }
}
