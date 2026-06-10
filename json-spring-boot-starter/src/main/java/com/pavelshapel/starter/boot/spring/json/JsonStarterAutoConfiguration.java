package com.pavelshapel.starter.boot.spring.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
final class JsonStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(JsonStarterAutoConfiguration.class);

  JsonStarterAutoConfiguration() {
    log.info("json-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnClass(ObjectMapper.class)
  JsonConverter jacksonJsonConverter(ObjectMapper objectMapper) {
    return new JacksonJsonConverter(objectMapper);
  }
}
