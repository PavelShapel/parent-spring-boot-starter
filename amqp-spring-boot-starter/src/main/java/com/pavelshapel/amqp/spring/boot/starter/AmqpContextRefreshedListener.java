package com.pavelshapel.amqp.spring.boot.starter;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.amqp.spring.boot.starter.StarterAutoConfiguration.TYPE;

@Log4j2
public class AmqpContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(String.format("%s-spring-boot-starter was applied", TYPE));
    }
}