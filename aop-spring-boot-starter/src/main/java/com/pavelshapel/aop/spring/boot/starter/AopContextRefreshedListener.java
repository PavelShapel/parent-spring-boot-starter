package com.pavelshapel.aop.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration.TYPE;

@Slf4j
public class AopContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("{}-spring-boot-starter was applied", TYPE);
    }
}