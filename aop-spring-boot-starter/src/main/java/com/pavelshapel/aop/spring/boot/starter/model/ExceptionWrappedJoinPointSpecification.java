package com.pavelshapel.aop.spring.boot.starter.model;

import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import org.aspectj.lang.JoinPoint;

public class ExceptionWrappedJoinPointSpecification extends AbstractJoinPointSpecification<ExceptionWrapped> {
    public ExceptionWrappedJoinPointSpecification(JoinPoint joinPoint, Class<ExceptionWrapped> annotationClass) {
        super(joinPoint, annotationClass);
    }
}