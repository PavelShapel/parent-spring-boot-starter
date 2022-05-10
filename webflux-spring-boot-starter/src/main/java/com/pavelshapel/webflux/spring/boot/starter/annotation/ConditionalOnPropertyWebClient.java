package com.pavelshapel.webflux.spring.boot.starter.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

import static com.pavelshapel.webflux.spring.boot.starter.properties.WebFluxProperties.PREFIX;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@ConditionalOnProperty(prefix = PREFIX, name = ConditionalOnPropertyWebClient.WEB_CLIENT + ".enabled", havingValue = "true")
public @interface ConditionalOnPropertyWebClient {
    String WEB_CLIENT = "web-client";
}
