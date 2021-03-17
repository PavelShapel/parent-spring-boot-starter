package com.pavelshapel.aop.spring.boot.starter.log.method.result;

import com.pavelshapel.aop.spring.boot.starter.log.method.LogSpecification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.SUCCESS_RESULT;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@LogSpecification(successPrefix = SUCCESS_RESULT)
public @interface LogMethodResult {
}