package com.pavelshapel.jpa.spring.boot.starter;

import com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator.JpaDecorateAnnotationBeanPostProcessor;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator.ThrowableDecoratorJpaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "jpa";

    @Bean
    public JpaContextRefreshedListener jpaContextRefreshedListener() {
        return new JpaContextRefreshedListener();
    }

    @Bean
    public JpaDecorateAnnotationBeanPostProcessor jpaDecorateAnnotationBeanPostProcessor() {
        return new JpaDecorateAnnotationBeanPostProcessor();
    }

    @Bean
    public ThrowableDecoratorJpaService throwableDecoratorJpaService() {
        return new ThrowableDecoratorJpaService();
    }
}