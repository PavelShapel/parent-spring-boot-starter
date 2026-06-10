package com.pavelshapel.starter.boot.spring.zone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = ZoneStarterAutoConfiguration.class)
class ZoneStarterAutoConfigurationTest {
  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadZoneRegistryBeanIntoContext() {
    boolean result = applicationContext.containsBean("zoneRegistry");

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnZoneRegistryInstance() {
    ZoneRegistry zoneRegistry = applicationContext.getBean(ZoneRegistry.class);

    assertThat(zoneRegistry).isInstanceOf(ZoneRegistry.class);
  }

  @Test
  void shouldCreateSingletonZoneRegistryBean() {
    ZoneRegistry zoneRegistryOne = applicationContext.getBean(ZoneRegistry.class);
    ZoneRegistry zoneRegistryTwo = applicationContext.getBean(ZoneRegistry.class);

    assertThat(zoneRegistryOne).isSameAs(zoneRegistryTwo);
  }

  @Test
  void shouldHaveZoneRegistryBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("zoneRegistry")).isTrue();
  }
}
