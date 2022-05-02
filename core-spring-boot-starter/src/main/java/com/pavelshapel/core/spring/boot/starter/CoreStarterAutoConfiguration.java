package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.api.util.*;
import com.pavelshapel.core.spring.boot.starter.bpp.SelfAutowiredAnnotationBeanPostProcessor;
import com.pavelshapel.core.spring.boot.starter.api.annotation.AnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.impl.annotation.ClassAnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.impl.service.decorator.DecorateDaoServiceAnnotationBeanPostProcessor;
import com.pavelshapel.core.spring.boot.starter.impl.util.*;
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
        return new CoreStreamUtils();
    }

    @Bean
    public ClassUtils classUtils() {
        return new CoreClassUtils();
    }

    @Bean
    public RandomUtils randomUtils() {
        return new CoreRandomUtils();
    }

    @Bean
    public CompletableFutureUtils completableFutureUtils() {
        return new CoreCompletableFutureUtils();
    }

    @Bean
    public SubstitutionUtils substitutionUtils(){
        return new CoreSubstitutionUtils();
    }

    @Bean
    public SelfAutowiredAnnotationBeanPostProcessor selfAutowiredAnnotationBeanPostProcessor() {
        return new SelfAutowiredAnnotationBeanPostProcessor();
    }

    @Bean
    public DecorateDaoServiceAnnotationBeanPostProcessor daoDecorateAnnotationBeanPostProcessor() {
        return new DecorateDaoServiceAnnotationBeanPostProcessor();
    }
}