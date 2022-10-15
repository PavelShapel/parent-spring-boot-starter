package com.pavelshapel.aop.spring.boot.starter.impl;

import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aop.spring.boot.starter.model.ExceptionWrappedJoinPointSpecification;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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

    @SneakyThrows
    @Around(POINTCUT)
    public Object onDuration(ProceedingJoinPoint joinPoint) {
        ExceptionWrappedJoinPointSpecification exceptionWrappedJoinPointSpecification = createExceptionWrappedJoinPointSpecification(joinPoint);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Exception exception) {
            String methodArguments = exceptionWrappedJoinPointSpecification.getMethodArguments().toString();
            throw exceptionWrappedJoinPointSpecification.getAnnotation().value().getConstructor(String.class).newInstance(methodArguments);
        }
        return proceed;
    }

    private ExceptionWrappedJoinPointSpecification createExceptionWrappedJoinPointSpecification(JoinPoint joinPoint) {
        return new ExceptionWrappedJoinPointSpecification(joinPoint, ExceptionWrapped.class);
    }
}