package com.pavelshapel.starter.boot.spring.log;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.lang.reflect.Member;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@AutoConfiguration
final class LogStarterAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(LogStarterAutoConfiguration.class);

  LogStarterAutoConfiguration() {
    log.info("log-spring-boot-starter was applied ✅");
  }

  @Bean
  @Scope(SCOPE_PROTOTYPE)
  Logger logger(InjectionPoint injectionPoint) {
    return Optional.ofNullable(injectionPoint)
        .map(InjectionPoint::getMember)
        .map(Member::getDeclaringClass)
        .map(LoggerFactory::getLogger)
        .orElseThrow(() -> new BeanInitializationException("Could not initialize logger"));
  }
}
