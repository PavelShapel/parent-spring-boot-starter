package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.reflection.annotation.AnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.reflection.annotation.ClassAnnotationReplacer;
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
}