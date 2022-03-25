package com.pavelshapel.core.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration.TYPE;
import static java.util.logging.Level.INFO;

@SuppressWarnings("NullableProblems")
@Log
public class CoreContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{0}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.log(INFO, LOG_PATTERN, TYPE);
    }
}