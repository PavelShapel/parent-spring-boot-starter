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
public class LoggableJoinPointSpecification {
    String className;
    String methodName;
    Loggable loggable;

    public LoggableJoinPointSpecification(@NonNull JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        methodName = method.getName();
        Class<?> joinPointClass = joinPoint.getTarget().getClass();
        className = joinPointClass.getSimpleName();
        loggable = Optional.ofNullable(method.getAnnotation(Loggable.class))
                .orElseGet(() -> joinPointClass.getAnnotation(Loggable.class));
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