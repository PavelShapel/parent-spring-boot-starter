package com.pavelshapel.aop.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractJoinPointSpecification<T extends Annotation> {
    @Getter(AccessLevel.NONE)
    Method method;
    @Getter(AccessLevel.NONE)
    Class<?> targetClass;
    String className;
    String methodName;
    Map<String, String> methodArguments;
    T annotation;

    protected AbstractJoinPointSpecification(JoinPoint joinPoint, Class<T> annotationClass) {
        method = getMethod(joinPoint);
        targetClass = getTargetClass(joinPoint);
        methodName = method.getName();
        className = targetClass.getSimpleName();
        annotation = getAnnotation(annotationClass);
        methodArguments = getMethodArguments(joinPoint);
    }

    private Method getMethod(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getSignature)
                .filter(MethodSignature.class::isInstance)
                .map(MethodSignature.class::cast)
                .map(MethodSignature::getMethod)
                .orElseThrow();
    }

    private Class<?> getTargetClass(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getTarget)
                .map(Object::getClass)
                .orElseThrow();
    }

    private T getAnnotation(Class<T> annotationClass) {
        return Optional.ofNullable(method.getAnnotation(annotationClass))
                .orElseGet(() -> targetClass.getAnnotation(annotationClass));
    }

    private Map<String, String> getMethodArguments(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        return IntStream.range(0, args.length)
                .boxed()
                .collect(Collectors.toMap(
                        Object::toString,
                        index -> args[index].toString()
                ));
    }
}