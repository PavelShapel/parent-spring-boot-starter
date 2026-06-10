package com.pavelshapel.starter.boot.spring.bot.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = BotApiStarterAutoConfiguration.class)
class BotApiStarterAutoConfigurationTest {
  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadBotMessageSourceServiceBeanIntoContext() {
    boolean result = applicationContext.containsBean("botMessageSourceService");

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnBotMessageSourceServiceInstance() {
    BotMessageSourceService botMessageSourceService =
        applicationContext.getBean(BotMessageSourceService.class);

    assertThat(botMessageSourceService).isInstanceOf(BotMessageSourceService.class);
  }

  @Test
  void shouldCreateSingletonBotMessageSourceServiceBean() {
    BotMessageSourceService botMessageSourceServiceOne =
        applicationContext.getBean(BotMessageSourceService.class);
    BotMessageSourceService botMessageSourceServiceTwo =
        applicationContext.getBean(BotMessageSourceService.class);

    assertThat(botMessageSourceServiceOne).isSameAs(botMessageSourceServiceTwo);
  }

  @Test
  void shouldHaveBotMessageSourceServiceBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("botMessageSourceService")).isTrue();
  }
}
