package com.pavelshapel.aop.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.logging.Level;

import static com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration.TYPE;

@SuppressWarnings("NullableProblems")
@Log
public class AopContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{0}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.log(Level.INFO, LOG_PATTERN, TYPE);
    }
}