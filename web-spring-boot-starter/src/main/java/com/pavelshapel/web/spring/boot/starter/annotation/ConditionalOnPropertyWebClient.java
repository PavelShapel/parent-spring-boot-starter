package com.pavelshapel.web.spring.boot.starter.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

import static com.pavelshapel.web.spring.boot.starter.properties.WebProperties.PREFIX;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@ConditionalOnProperty(prefix = PREFIX, name = ConditionalOnPropertyWebClient.WEB_CLIENT)
public @interface ConditionalOnPropertyWebClient {
    String WEB_CLIENT = "web-client";
}
