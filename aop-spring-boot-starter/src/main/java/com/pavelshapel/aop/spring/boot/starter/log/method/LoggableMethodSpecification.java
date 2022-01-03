package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class LoggableMethodSpecification {
    Method method;
    String methodDeclaringClassName;
    String methodName;
    Loggable loggable;

    public LoggableMethodSpecification(@NonNull JoinPoint joinPoint) {
        this.method = getMethod(joinPoint);
        Class<?> declaringClass = this.method.getDeclaringClass();
        this.methodDeclaringClassName = declaringClass.getSimpleName();
        this.methodName = this.method.getName();
        this.loggable = Optional.ofNullable(this.method.getAnnotation(Loggable.class))
                .orElseGet(() -> declaringClass.getAnnotation(Loggable.class));
    }

    private Method getMethod(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getSignature)
                .filter(MethodSignature.class::isInstance)
                .map(MethodSignature.class::cast)
                .map(MethodSignature::getMethod)
                .orElseThrow(UnsupportedOperationException::new);
    }
}