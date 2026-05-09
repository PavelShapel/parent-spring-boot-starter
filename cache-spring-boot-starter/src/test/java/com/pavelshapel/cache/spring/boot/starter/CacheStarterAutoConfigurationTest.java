package com.pavelshapel.cache.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CacheStarterAutoConfiguration.class)
@ActiveProfiles("default")
class CacheStarterAutoConfigurationTest {

  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadCacheManagerBeanIntoContext() {
    assertThat(applicationContext.containsBean("cacheManager")).isTrue();
  }

  @Test
  void shouldReturnCacheManagerInstance() {
    CacheManager cacheManager = applicationContext.getBean(CacheManager.class);

    assertThat(cacheManager).isInstanceOf(CacheManager.class);
  }

  @Test
  void shouldCreateSingletonCacheManagerBean() {
    CacheManager cacheManagerOne = applicationContext.getBean(CacheManager.class);
    CacheManager cacheManagerTwo = applicationContext.getBean(CacheManager.class);

    assertThat(cacheManagerOne).isSameAs(cacheManagerTwo);
  }

  @Test
  void shouldHaveCacheManagerBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("cacheManager")).isTrue();
  }
}
