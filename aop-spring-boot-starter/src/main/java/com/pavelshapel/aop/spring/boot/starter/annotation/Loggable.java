package com.pavelshapel.aop.spring.boot.starter.annotation;

import com.pavelshapel.aop.spring.boot.starter.model.LoggableType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Loggable {
    LoggableType[] value() default {LoggableType.METHOD_RESULT, LoggableType.METHOD_EXCEPTION};

    String level() default "INFO";
}