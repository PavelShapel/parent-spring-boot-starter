package com.pavelshapel.stream.spring.boot.starter;

import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamStarterAutoConfiguration {
    public static final String TYPE = "stream";

    @Bean
    public StreamContextRefreshedListener streamContextRefreshedListener() {
        return new StreamContextRefreshedListener();
    }

    @Bean
    public StreamUtils streamUtils() {
        return new StreamUtils();
    }
}