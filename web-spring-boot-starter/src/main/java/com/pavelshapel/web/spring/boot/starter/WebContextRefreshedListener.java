package com.pavelshapel.web.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.web.spring.boot.starter.WebStarterAutoConfiguration.TYPE;

@Slf4j
public class WebContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(String.format("%s-spring-boot-starter was applied", TYPE));
    }
}