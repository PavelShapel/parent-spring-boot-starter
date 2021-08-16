package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Optional;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggableMethodSpecification {
    Method method;
    String methodDeclaringClassName;
    String methodName;
    Loggable loggable;

    public LoggableMethodSpecification(@NonNull JoinPoint joinPoint) {
        this.method = getMethod(joinPoint);
        initializeMethodSpecification();
    }

    public LoggableMethodSpecification(@NonNull Method method) {
        this.method = method;
        initializeMethodSpecification();
    }

    private void initializeMethodSpecification() {
        this.methodDeclaringClassName = this.method.getDeclaringClass().getSimpleName();
        this.methodName = this.method.getName();
        this.loggable = Optional.ofNullable(this.method.getAnnotation(Loggable.class))
                .orElse(this.method.getDeclaringClass().getAnnotation(Loggable.class));
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }
}