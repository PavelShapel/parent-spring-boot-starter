package com.pavelshapel.cache.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.cache.spring.boot.starter.CacheStarterAutoConfiguration.TYPE;
import static java.util.logging.Level.INFO;

@SuppressWarnings("NullableProblems")
@Log
public class CacheContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.log(INFO, LOG_PATTERN, TYPE);
    }
}