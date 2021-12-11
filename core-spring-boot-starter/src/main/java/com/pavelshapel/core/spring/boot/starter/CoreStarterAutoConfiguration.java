package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.reflection.annotation.AnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.reflection.annotation.ClassAnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.util.CommonUtils;
import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreStarterAutoConfiguration {
    public static final String TYPE = "core";

    @Bean
    public CoreContextRefreshedListener coreContextRefreshedListener() {
        return new CoreContextRefreshedListener();
    }

    @Bean
    public AnnotationReplacer annotationReplacer() {
        return new ClassAnnotationReplacer();
    }

    @Bean
    public StreamUtils streamUtils() {
        return new StreamUtils();
    }

    @Bean
    public CommonUtils commonUtils() {
        return new CommonUtils();
    }
}