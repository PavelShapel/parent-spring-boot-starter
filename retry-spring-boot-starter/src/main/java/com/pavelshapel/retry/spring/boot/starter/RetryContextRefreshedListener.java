package com.pavelshapel.retry.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import static com.pavelshapel.retry.spring.boot.starter.RetryStarterAutoConfiguration.TYPE;
import static java.util.logging.Level.INFO;

@Log
public class RetryContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{0}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        log.log(INFO, LOG_PATTERN, TYPE);
    }
}