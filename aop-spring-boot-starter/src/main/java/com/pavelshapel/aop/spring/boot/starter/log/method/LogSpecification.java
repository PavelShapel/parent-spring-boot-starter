package com.pavelshapel.aop.spring.boot.starter.log.method;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.SUCCESS;
import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.THREW_AN_EXCEPTION;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogSpecification {
    Level level() default Level.INFO;

    String successPrefix() default SUCCESS;

    String exceptionPrefix() default THREW_AN_EXCEPTION;
}