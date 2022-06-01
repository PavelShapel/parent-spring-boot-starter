package com.pavelshapel.jpa.spring.boot.starter;

import com.pavelshapel.jpa.spring.boot.starter.service.search.bfpp.SearchSpecificationBeanFactoryPostProcessor;
import com.pavelshapel.jpa.spring.boot.starter.service.decorator.DecorateDaoServiceAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaStarterAutoConfiguration {
    public static final String TYPE = "jpa";

    @Bean
    public JpaContextRefreshedListener jpaContextRefreshedListener() {
        return new JpaContextRefreshedListener();
    }

    @Bean
    public static SearchSpecificationBeanFactoryPostProcessor searchSpecificationBeanFactoryPostProcessor() {
        return new SearchSpecificationBeanFactoryPostProcessor();
    }

    @Bean
    public DecorateDaoServiceAnnotationBeanPostProcessor daoDecorateAnnotationBeanPostProcessor() {
        return new DecorateDaoServiceAnnotationBeanPostProcessor();
    }
}