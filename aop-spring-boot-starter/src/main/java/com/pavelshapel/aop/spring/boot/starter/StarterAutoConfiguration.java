package com.pavelshapel.aop.spring.boot.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "aop";

    @Bean
    public AopContextRefreshedListener aopContextRefreshedListener() {
        return new AopContextRefreshedListener();
    }

//    @Bean
//    public LoggableAnnotationBeanPostProcessor loggableAnnotationBeanPostProcessor() {
//        return new LoggableAnnotationBeanPostProcessor();
//    }

//    @Bean
//    public LoggableAspect loggableAspect() {
//        return new LoggableAspect();
//    }
}