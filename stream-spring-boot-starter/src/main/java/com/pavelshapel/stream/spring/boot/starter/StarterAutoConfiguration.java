package com.pavelshapel.stream.spring.boot.starter;

import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StreamProperties.class)
public class StarterAutoConfiguration {
    public static final String TYPE = "stream";
    public static final String PREFIX = "spring.pavelshapel." + TYPE;
    public static final String PROPERTY_NAME = "stream-utils";
    public static final String TRUE = "true";

    @Bean
    public StreamContextRefreshedListener streamContextRefreshedListener() {
        return new StreamContextRefreshedListener();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public StreamUtils streamUtils() {
        return new StreamUtils();
    }
}