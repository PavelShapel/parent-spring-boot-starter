package com.pavelshapel.starter.boot.spring.timezone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = TimezoneStarterAutoConfiguration.class)
class TimezoneStarterAutoConfigurationTest {
  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadTimezoneRegistryBeanIntoContext() {
    boolean result = applicationContext.containsBean("timezoneRegistry");

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnTimezoneRegistryInstance() {
    TimezoneRegistry timezoneRegistry = applicationContext.getBean(TimezoneRegistry.class);

    assertThat(timezoneRegistry).isInstanceOf(TimezoneRegistry.class);
  }

  @Test
  void shouldCreateSingletonTimezoneRegistryBean() {
    TimezoneRegistry timezoneRegistryOne = applicationContext.getBean(TimezoneRegistry.class);
    TimezoneRegistry timezoneRegistryTwo = applicationContext.getBean(TimezoneRegistry.class);

    assertThat(timezoneRegistryOne).isSameAs(timezoneRegistryTwo);
  }

  @Test
  void shouldHaveTimezoneRegistryBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("timezoneRegistry")).isTrue();
  }
}
