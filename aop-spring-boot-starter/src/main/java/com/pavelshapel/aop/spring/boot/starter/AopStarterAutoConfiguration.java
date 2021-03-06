package com.pavelshapel.aop.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.log.method.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopStarterAutoConfiguration {
    public static final String TYPE = "aop";

    @Bean
    public AopContextRefreshedListener aopContextRefreshedListener() {
        return new AopContextRefreshedListener();
    }

    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}