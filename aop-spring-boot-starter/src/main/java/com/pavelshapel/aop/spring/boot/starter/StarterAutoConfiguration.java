package com.pavelshapel.aop.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.log.method.duration.MethodDurationAspectLog;
import com.pavelshapel.aop.spring.boot.starter.log.method.result.MethodResultAspectLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "aop";

    @Bean
    public AopContextRefreshedListener aopContextRefreshedListener() {
        return new AopContextRefreshedListener();
    }

    @Bean
    public MethodResultAspectLog methodResultAspectLog() {
        return new MethodResultAspectLog();
    }

    @Bean
    public MethodDurationAspectLog methodDurationAspectLog() {
        return new MethodDurationAspectLog();
    }
}