package com.pavelshapel.jpa.spring.boot.starter;

import com.pavelshapel.jpa.spring.boot.starter.service.search.bfpp.SearchSpecificationBeanFactoryPostProcessor;
import com.pavelshapel.jpa.spring.boot.starter.service.decorator.DecorateDaoServiceAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

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

    @Bean
    public AuditorAware<String> auditorProvider() {
        /*
          if you are using spring security, you can get the currently logged username with following code segment.
          SecurityContextHolder.getContext().getAuthentication().getName()
         */
        return () -> Optional.of("pavelshapel");
    }
}