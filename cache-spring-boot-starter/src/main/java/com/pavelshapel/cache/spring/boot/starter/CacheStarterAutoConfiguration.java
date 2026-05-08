package com.pavelshapel.cache.spring.boot.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
@EnableCaching
@Profile("!test")
public class CacheStarterAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CacheStarterAutoConfiguration.class);

    CacheStarterAutoConfiguration() {
        log.info("cache-spring-boot-starter was applied ✅");
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}