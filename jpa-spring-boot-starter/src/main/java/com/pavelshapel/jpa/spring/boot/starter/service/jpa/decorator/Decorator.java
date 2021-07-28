package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Decorator {
}
