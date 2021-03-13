package com.pavelshapel.core.spring.boot.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "core";

    @Bean
    public CoreContextRefreshedListener coreContextRefreshedListener() {
        return new CoreContextRefreshedListener();
    }
}