package com.pavelshapel.aop.spring.boot.starter.log.method.result;

import com.pavelshapel.aop.spring.boot.starter.log.method.LogSpecification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@LogSpecification(successPrefix = "returned value")
public @interface LogMethodResult {
}