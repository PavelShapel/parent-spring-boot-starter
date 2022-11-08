package com.pavelshapel.aop.spring.boot.starter.impl;

import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aop.spring.boot.starter.model.ExceptionWrappedJoinPointSpecification;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Optional;

@Aspect
public class ExceptionWrappedAspect {
    private static final String ANNOTATION = "com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped";
    private static final String POINTCUT_METHOD_ANNOTATION = "@annotation(" + ANNOTATION + ")";
    private static final String POINTCUT_CLASS_ANNOTATION = "@within(" + ANNOTATION + ")";
    private static final String POINTCUT = "execution(* *(..)) && (annotatedMethod() || annotatedClass())";

    @Pointcut(POINTCUT_METHOD_ANNOTATION)
    public void annotatedMethod() {
        //pointcut
    }

    @Pointcut(POINTCUT_CLASS_ANNOTATION)
    public void annotatedClass() {
        //pointcut
    }

    @Around(POINTCUT)
    public Object onDuration(ProceedingJoinPoint joinPoint) {
        ExceptionWrappedJoinPointSpecification exceptionWrappedJoinPointSpecification = createExceptionWrappedJoinPointSpecification(joinPoint);
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable exception) {
            throwWrappedException(exceptionWrappedJoinPointSpecification, exception);
        }
        return proceed;
    }

    @SneakyThrows
    private void throwWrappedException(ExceptionWrappedJoinPointSpecification exceptionWrappedJoinPointSpecification, Throwable exception) {
        String message = String.format(
                "%s method parameters %s, cause [%s]",
                getPrefix(exceptionWrappedJoinPointSpecification),
                joinParameters(exceptionWrappedJoinPointSpecification),
                exception.toString());
        throw exceptionWrappedJoinPointSpecification.getAnnotation().value().getConstructor(String.class, Throwable.class).newInstance(message, exception);
    }

    private String getPrefix(ExceptionWrappedJoinPointSpecification exceptionWrappedJoinPointSpecification) {
        return Optional.of(exceptionWrappedJoinPointSpecification)
                .map(ExceptionWrappedJoinPointSpecification::getAnnotation)
                .map(ExceptionWrapped::prefix)
                .orElseThrow();
    }

    private String joinParameters(ExceptionWrappedJoinPointSpecification exceptionWrappedJoinPointSpecification) {
        return Optional.of(exceptionWrappedJoinPointSpecification)
                .map(ExceptionWrappedJoinPointSpecification::getMethodParameters)
                .map(Object::toString)
                .orElseThrow();
    }

    private ExceptionWrappedJoinPointSpecification createExceptionWrappedJoinPointSpecification(JoinPoint joinPoint) {
        return new ExceptionWrappedJoinPointSpecification(joinPoint, ExceptionWrapped.class);
    }
}