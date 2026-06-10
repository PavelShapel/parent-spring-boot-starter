package com.pavelshapel.starter.boot.spring.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@AutoConfiguration
final class I18nStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(I18nStarterAutoConfiguration.class);

  I18nStarterAutoConfiguration() {
    log.info("i18n-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
