package com.pavelshapel.random.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import java.util.logging.Level;

import static com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration.TYPE;

@Log
public class RandomContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        log.log(Level.INFO, "{0}-spring-boot-starter was applied", TYPE);
    }
}