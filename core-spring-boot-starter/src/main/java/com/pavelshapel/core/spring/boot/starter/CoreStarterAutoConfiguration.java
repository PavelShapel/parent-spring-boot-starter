package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.bpp.annotation.autowired.SelfAutowiredAnnotationBeanPostProcessor;
import com.pavelshapel.core.spring.boot.starter.reflection.annotation.replacer.AnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.reflection.annotation.replacer.ClassAnnotationReplacer;
import com.pavelshapel.core.spring.boot.starter.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.util.CompletableFutureUtils;
import com.pavelshapel.core.spring.boot.starter.util.RandomUtils;
import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import com.pavelshapel.core.spring.boot.starter.util.impl.CoreClassUtils;
import com.pavelshapel.core.spring.boot.starter.util.impl.CoreCompletableFutureUtils;
import com.pavelshapel.core.spring.boot.starter.util.impl.CoreStreamUtils;
import com.pavelshapel.core.spring.boot.starter.util.impl.CoreRandomUtils;
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
    public SelfAutowiredAnnotationBeanPostProcessor selfAutowiredAnnotationBeanPostProcessor() {
        return new SelfAutowiredAnnotationBeanPostProcessor();
    }
}