package com.pavelshapel.aop.spring.boot.starter.log.method.duration;

import com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog;
import com.pavelshapel.aop.spring.boot.starter.log.method.LogSpecification;
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
    public Object callLogMethodDuration(ProceedingJoinPoint joinPoint) {
        final long startTimeMillis = System.currentTimeMillis();
        final Object proceed = joinPoint.proceed();
        final long durationOfExecution = System.currentTimeMillis() - startTimeMillis;
        logDuration(joinPoint, durationOfExecution);
        return proceed;
    }

    private void logDuration(ProceedingJoinPoint joinPoint, long durationOfExecution) {
        final MethodSpecification methodSpecification = new MethodSpecification(joinPoint, LogMethodDuration.class);
        final LogSpecification logSpecification = methodSpecification.getLogSpecification();
        final Level level = Level.toLevel(logSpecification.level().toString());
        log.log(level,
                LOG_PATTERN,
                methodSpecification.getMethodDeclaringClassName(),
                methodSpecification.getMethodName(),
                logSpecification.successPrefix(),
                getVerifiedLogResult(String.format("%d ms", durationOfExecution))
        );
    }
}