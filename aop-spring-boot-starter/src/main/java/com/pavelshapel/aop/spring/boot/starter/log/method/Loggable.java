package com.pavelshapel.aop.spring.boot.starter.log.method;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Loggable {
    LoggableType[] value() default {LoggableType.METHOD_RESULT, LoggableType.METHOD_EXCEPTION};

    String level() default "INFO";
}