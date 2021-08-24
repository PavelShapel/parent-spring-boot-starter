package com.pavelshapel.cache.spring.boot.starter;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheStarterAutoConfiguration {
    public static final String TYPE = "cache";

    @Bean
    public CacheContextRefreshedListener cacheContextRefreshedListener() {
        return new CacheContextRefreshedListener();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}