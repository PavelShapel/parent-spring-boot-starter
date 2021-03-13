package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.NonNull;
import lombok.Value;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

@Value
public class MethodSpecification {
    Method method;
    String methodDeclaringClassName;
    String methodName;
    LogSpecification logSpecification;

    public MethodSpecification(@NonNull JoinPoint joinPoint, Class<?> annotationClass) {
        this.method = getMethod(joinPoint);
        this.methodDeclaringClassName = method.getDeclaringClass().getSimpleName();
        this.methodName = method.getName();
        this.logSpecification = AnnotationUtils.findAnnotation(annotationClass, LogSpecification.class);
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }
}