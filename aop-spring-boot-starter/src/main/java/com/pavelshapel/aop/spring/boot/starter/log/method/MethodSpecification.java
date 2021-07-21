package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.NonNull;
import lombok.Value;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Value
public class MethodSpecification {
    Method method;
    String methodDeclaringClassName;
    String methodName;
    Loggable loggable;

    public MethodSpecification(@NonNull JoinPoint joinPoint) {
        this.method = getMethod(joinPoint);
        this.methodDeclaringClassName = method.getDeclaringClass().getSimpleName();
        this.methodName = method.getName();
        this.loggable = method.getAnnotation(Loggable.class);
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }
}