package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoggableMethodSpecification {
    Method method;
    String methodDeclaringClassName;
    String methodName;
    Loggable loggable;

    public LoggableMethodSpecification(@NonNull JoinPoint joinPoint) {
        method = getMethod(joinPoint);
        Class<?> declaringClass = method.getDeclaringClass();
        methodDeclaringClassName = declaringClass.getSimpleName();
        methodName = method.getName();
        loggable = Optional.ofNullable(method.getAnnotation(Loggable.class))
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