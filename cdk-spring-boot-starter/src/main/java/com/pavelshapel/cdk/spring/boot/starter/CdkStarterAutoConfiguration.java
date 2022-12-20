package com.pavelshapel.cdk.spring.boot.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdkStarterAutoConfiguration {
    public static final String TYPE = "cdk";

    @Bean
    public CdkContextRefreshedListener cdkContextRefreshedListener() {
        return new CdkContextRefreshedListener();
    }
}
