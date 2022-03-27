package com.pavelshapel.random.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.logging.Level;

import static com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration.TYPE;

@SuppressWarnings("NullableProblems")
@Log
public class RandomContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.log(Level.INFO, "{0}-spring-boot-starter was applied", TYPE);
    }
}