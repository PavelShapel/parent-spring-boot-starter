package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.properties.BotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(BotProperties.class)
final class BotApiStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(BotApiStarterAutoConfiguration.class);

  BotApiStarterAutoConfiguration() {
    log.info("bot-api-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  BotMessageSourceService botMessageSourceService(MessageSource messageSource) {
    return new BotMessageSourceService(messageSource);
  }
}
