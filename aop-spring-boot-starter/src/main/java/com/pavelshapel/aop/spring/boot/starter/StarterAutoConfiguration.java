package com.pavelshapel.aop.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.log.method.duration.MethodDurationAspectLog;
import com.pavelshapel.aop.spring.boot.starter.log.method.result.MethodResultAspectLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AopProperties.class)
public class StarterAutoConfiguration {
    public static final String TYPE = "aop";
    public static final String PREFIX = "spring.pavelshapel." + TYPE;
    public static final String TRUE = "true";

    @Bean
    public AopContextRefreshedListener aopContextRefreshedListener() {
        return new AopContextRefreshedListener();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = "log-method-result", havingValue = TRUE)
    public MethodResultAspectLog methodResultAspectLog() {
        return new MethodResultAspectLog();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = "log-method-duration", havingValue = TRUE)
    public MethodDurationAspectLog methodDurationAspectLog() {
        return new MethodDurationAspectLog();
    }
}