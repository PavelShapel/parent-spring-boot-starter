package com.pavelshapel.kafka.spring.boot.starter;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.pavelshapel.kafka.spring.boot.starter.KafkaStarterAutoConfiguration.TYPE;
import static java.util.logging.Level.INFO;


@SuppressWarnings("NullableProblems")
@Log
public class KafkaContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String LOG_PATTERN = "{0}-spring-boot-starter was applied";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.log(INFO, LOG_PATTERN, TYPE);
    }
}