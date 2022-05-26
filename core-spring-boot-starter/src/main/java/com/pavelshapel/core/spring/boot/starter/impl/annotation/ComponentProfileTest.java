package com.pavelshapel.core.spring.boot.starter.impl.annotation;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
@Profile("test")
public @interface ComponentProfileTest {
}