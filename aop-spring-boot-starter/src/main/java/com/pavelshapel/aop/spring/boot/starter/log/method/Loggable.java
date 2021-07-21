package com.pavelshapel.aop.spring.boot.starter.log.method;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    LoggableType[] value() default {LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION};

    Level level() default Level.INFO;
}