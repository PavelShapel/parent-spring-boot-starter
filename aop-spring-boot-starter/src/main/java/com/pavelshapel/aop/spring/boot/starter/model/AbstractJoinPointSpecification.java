package com.pavelshapel.aop.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractJoinPointSpecification<T extends Annotation> {
    @Getter(AccessLevel.NONE)
    Method method;
    @Getter(AccessLevel.NONE)
    Class<?> targetClass;
    String className;
    String methodName;
    List<MethodParameter> methodParameters;
    T annotation;

    protected AbstractJoinPointSpecification(JoinPoint joinPoint, Class<T> annotationClass) {
        targetClass = getTargetClass(joinPoint);
        methodParameters = getMethodParameters(joinPoint);
        method = getMethod(joinPoint);
        methodName = method.getName();
        className = targetClass.getSimpleName();
        annotation = getAnnotation(annotationClass);
    }

    private Class<?> getTargetClass(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getTarget)
                .map(Object::getClass)
                .orElseThrow();
    }

    private List<MethodParameter> getMethodParameters(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        return IntStream.range(0, args.length)
                .boxed()
                .map(index -> createMethodArgument(index, args[index]))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private MethodParameter createMethodArgument(Integer index, Object argument) {
        return MethodParameter.builder()
                .index(index)
                .value(argument)
                .build();
    }

    private Method getMethod(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getSignature)
                .map(Signature::getName)
                .map(name -> ReflectionUtils.findMethod(targetClass, name, getMethodParameterTypes(joinPoint)))
                .orElseThrow();
    }

    private Class<?>[] getMethodParameterTypes(JoinPoint joinPoint) {
        return Optional.ofNullable(joinPoint)
                .map(JoinPoint::getSignature)
                .filter(MethodSignature.class::isInstance)
                .map(MethodSignature.class::cast)
                .map(MethodSignature::getMethod)
                .map(Method::getParameterTypes)
                .orElseThrow();
    }


    private T getAnnotation(Class<T> annotationClass) {
        return Optional.ofNullable(findAnnotation(method, annotationClass))
                .orElseGet(() -> findAnnotation(targetClass, annotationClass));
    }
}