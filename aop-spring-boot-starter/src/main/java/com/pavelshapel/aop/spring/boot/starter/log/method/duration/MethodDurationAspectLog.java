package com.pavelshapel.aop.spring.boot.starter.log.method.duration;

import com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog;
import com.pavelshapel.aop.spring.boot.starter.log.method.MethodSpecification;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Log4j2
public class MethodDurationAspectLog extends AbstractAspectLog {
    @SneakyThrows
    @Around("@annotation(LogMethodDuration)")
    public Object callLogMethodDuration(ProceedingJoinPoint joinPoint){
        final long startTimeMillis = System.currentTimeMillis();

        final Object proceed = joinPoint.proceed();

        final long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;
        MethodSpecification methodSpecification = new MethodSpecification(joinPoint,LogMethodDuration.class);
        final Level level = Level.toLevel(methodSpecification.getLogSpecification().level().toString());

        log.log(level,
                LOG_PATTERN,
                methodSpecification.getMethodDeclaringClassName(),
                methodSpecification.getMethodName(),
                methodSpecification.getLogSpecification().successPrefix(),
                getVerifiedLogResult(String.format("%d ms", executionTimeMillis))
        );

        return proceed;
    }

}