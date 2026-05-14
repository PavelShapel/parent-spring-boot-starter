package com.pavelshapel.log.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootTest(
    classes = {
      LogStarterAutoConfiguration.class,
      LogStarterAutoConfigurationTest.LoggerProvider1.class,
      LogStarterAutoConfigurationTest.LoggerProvider2.class
    })
class LogStarterAutoConfigurationTest {
  @Component
  static class LoggerProvider1 implements LoggerProvider {
    private final Logger logger;

    LoggerProvider1(Logger logger) {
      this.logger = logger;
    }

    @Override
    public Logger getLogger() {
      return logger;
    }
  }

  @Component
  static class LoggerProvider2 implements LoggerProvider {
    private final Logger logger;

    LoggerProvider2(Logger logger) {
      this.logger = logger;
    }

    @Override
    public Logger getLogger() {
      return logger;
    }
  }

  @Autowired private ApplicationContext applicationContext;

  @Autowired private LoggerProvider1 loggerProvider1;

  @Autowired private LoggerProvider2 loggerProvider2;

  @Test
  void shouldLoadLoggerBeanIntoContext() {
    boolean result = applicationContext.containsBean("logger");

    assertThat(result).isTrue();
  }

  @Test
  void shouldHaveLoggerBeanWithPrototypeScope() {
    assertThat(applicationContext.isPrototype("logger")).isTrue();
  }

  @Test
  void shouldReturnLoggerInstance() {
    Logger logger = loggerProvider1.getLogger();

    assertThat(logger).isNotNull().isInstanceOf(Logger.class);
  }

  @Test
  void shouldCreateNewLoggerBeanEachTime() {
    Logger loggerOne = loggerProvider1.getLogger();
    Logger loggerTwo = loggerProvider2.getLogger();

    assertThat(loggerOne).isNotSameAs(loggerTwo);
  }
}
