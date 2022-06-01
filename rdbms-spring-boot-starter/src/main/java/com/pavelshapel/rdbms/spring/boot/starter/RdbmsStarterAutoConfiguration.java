package com.pavelshapel.rdbms.spring.boot.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class RdbmsStarterAutoConfiguration {
    public static final String TYPE = "rdbms";

    @Bean
    public RdbmsContextRefreshedListener rdbmsContextRefreshedListener() {
        return new RdbmsContextRefreshedListener();
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