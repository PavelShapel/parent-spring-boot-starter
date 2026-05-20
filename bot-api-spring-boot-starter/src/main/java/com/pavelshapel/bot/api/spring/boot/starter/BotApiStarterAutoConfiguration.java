package com.pavelshapel.bot.api.spring.boot.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(BotProperties.class)
final class BotApiStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(BotApiStarterAutoConfiguration.class);

  BotApiStarterAutoConfiguration() {
    log.info("bot-api-spring-boot-starter was applied ✅");
  }
}
