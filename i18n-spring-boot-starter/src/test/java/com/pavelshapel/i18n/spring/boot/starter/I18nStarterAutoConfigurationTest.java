package com.pavelshapel.i18n.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootTest(classes = I18nStarterAutoConfiguration.class)
class I18nStarterAutoConfigurationTest {
  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadMessageSourceBeanIntoContext() {
    boolean result = applicationContext.containsBean("messageSource");

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnMessageSourceInstance() {
    MessageSource messageSource = applicationContext.getBean(MessageSource.class);

    assertThat(messageSource).isInstanceOf(MessageSource.class);
  }

  @Test
  void shouldCreateSingletonMessageSourceBean() {
    MessageSource messageSourceOne = applicationContext.getBean(MessageSource.class);
    MessageSource messageSourceTwo = applicationContext.getBean(MessageSource.class);

    assertThat(messageSourceOne).isSameAs(messageSourceTwo);
  }

  @Test
  void shouldHaveMessageSourceBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("messageSource")).isTrue();
  }

  @Test
  void shouldCreateMessageSourceBeanWithBasenameSet() {
    MessageSource messageSource = applicationContext.getBean(MessageSource.class);

    assertThat(messageSource).isInstanceOf(ReloadableResourceBundleMessageSource.class);
    ReloadableResourceBundleMessageSource reloadableMessageSource =
        (ReloadableResourceBundleMessageSource) messageSource;
    assertThat(reloadableMessageSource.getBasenameSet()).contains("classpath:i18n/messages");
  }
}
