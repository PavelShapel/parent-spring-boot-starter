package com.pavelshapel.starter.boot.spring.ordered;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
final class OrderedStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(OrderedStarterAutoConfiguration.class);

  OrderedStarterAutoConfiguration() {
    log.info("ordered-spring-boot-starter was applied ✅");
  }
}
