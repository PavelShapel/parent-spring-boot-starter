package com.pavelshapel.jpa.spring.boot.starter;

import com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator.JpaDecorateAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaStarterAutoConfiguration {
    public static final String TYPE = "jpa";

    @Bean
    public JpaContextRefreshedListener jpaContextRefreshedListener() {
        return new JpaContextRefreshedListener();
    }

    @Bean
    public JpaDecorateAnnotationBeanPostProcessor jpaDecorateAnnotationBeanPostProcessor() {
        return new JpaDecorateAnnotationBeanPostProcessor();
    }
}