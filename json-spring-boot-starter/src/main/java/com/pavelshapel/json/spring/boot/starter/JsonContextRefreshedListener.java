package com.pavelshapel.json.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.json.spring.boot.starter.StarterAutoConfiguration.TYPE;

@Slf4j
public class JsonContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(String.format("%s-spring-boot-starter was applied", TYPE));
    }
}