package com.pavelshapel.core.spring.boot.starter.impl.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Replaced {
    String value();
}