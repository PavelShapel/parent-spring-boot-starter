package com.pavelshapel.aop.spring.boot.starter.model;

import com.pavelshapel.aop.spring.boot.starter.annotation.Loggable;
import org.aspectj.lang.JoinPoint;

public class LoggableJoinPointSpecification extends AbstractJoinPointSpecification<Loggable> {
    public LoggableJoinPointSpecification(JoinPoint joinPoint, Class<Loggable> annotationClass) {
        super(joinPoint, annotationClass);
    }
}