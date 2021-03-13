package com.pavelshapel.web.spring.boot.starter;

import com.pavelshapel.web.spring.boot.starter.wrapper.TypedResponseWrapperRestControllerAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class StarterAutoConfiguration {
    public static final String TYPE = "web";
    public static final String PREFIX = "spring.pavelshapel." + TYPE;
    public static final String PROPERTY_NAME = "wrappers";
    public static final String TRUE = "true";

    @Bean
    public WebContextRefreshedListener webContextRefreshedListener() {
        return new WebContextRefreshedListener();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public TypedResponseWrapperRestControllerAdvice typedResponseWrapperRestControllerAdvice() {
        return new TypedResponseWrapperRestControllerAdvice();
    }
}