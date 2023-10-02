package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.CompletableFutureUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.MathUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.RandomUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import com.pavelshapel.core.spring.boot.starter.bpp.SelfAutowiredAnnotationBeanPostProcessor;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreClassUtils;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreCompletableFutureUtils;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreMathUtils;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreRandomUtils;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreStreamUtils;
import com.pavelshapel.core.spring.boot.starter.impl.util.CoreSubstitutionUtils;
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
    public SubstitutionUtils substitutionUtils() {
        return new CoreSubstitutionUtils();
    }

    @Bean
    public SelfAutowiredAnnotationBeanPostProcessor selfAutowiredAnnotationBeanPostProcessor() {
        return new SelfAutowiredAnnotationBeanPostProcessor();
    }

    @Bean
    public MathUtils mathUtils() {
        return new CoreMathUtils();
    }
}