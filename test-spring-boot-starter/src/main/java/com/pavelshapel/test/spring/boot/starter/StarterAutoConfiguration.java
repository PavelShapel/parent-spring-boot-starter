package com.pavelshapel.test.spring.boot.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "test";

    @Bean
    public TestContextRefreshedListener testContextRefreshedListener() {
        return new TestContextRefreshedListener();
    }
}