package com.pavelshapel.core.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration.TYPE;

@Slf4j
public class CoreContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(LOG_PATTERN, TYPE);
    }
}