package com.pavelshapel.aop.spring.boot.starter.log.method.duration;

import com.pavelshapel.aop.spring.boot.starter.log.method.LogSpecification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LogSpecification(successPrefix = "executed in")
public @interface LogMethodDuration {
}