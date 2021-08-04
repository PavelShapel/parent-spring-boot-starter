package com.pavelshapel.stream.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.stream.spring.boot.starter.StreamStarterAutoConfiguration.TYPE;

@Slf4j
public class StreamContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("{}-spring-boot-starter was applied", TYPE);
    }
}