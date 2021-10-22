package com.pavelshapel.test.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.test.spring.boot.starter.TestStarterAutoConfiguration.TYPE;

@Slf4j
public class TestContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(LOG_PATTERN, TYPE);
    }
}