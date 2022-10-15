package com.pavelshapel.aop.spring.boot.starter.impl;

import com.pavelshapel.aop.spring.boot.starter.annotation.Loggable;
import com.pavelshapel.aop.spring.boot.starter.model.LoggableJoinPointSpecification;
import com.pavelshapel.aop.spring.boot.starter.model.LoggableType;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.logging.Level;

import static java.util.Arrays.asList;
import static java.util.logging.Level.SEVERE;

@Aspect
@Log
public class LoggableAspect {
    private static final String LOG_PATTERN = "[{0}#{1}] {2} -> {3}";
    private static final String NOTHING_TO_LOG = "nothing to log";
    private static final String ANNOTATION = "com.pavelshapel.aop.spring.boot.starter.annotation.Loggable";
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

    @AfterReturning(pointcut = POINTCUT, returning = "result")
    public void onSuccess(JoinPoint joinPoint, Object result) {
        LoggableJoinPointSpecification loggableJoinPointSpecification = createLoggableJoinPointSpecification(joinPoint);
        if (isContainingLoggableType(loggableJoinPointSpecification, LoggableType.METHOD_RESULT)) {
            logSuccess(loggableJoinPointSpecification, result);
        }
    }

    private void logSuccess(LoggableJoinPointSpecification loggableJoinPointSpecification, Object result) {
        if (isResponseEntityErrorNotLogged(loggableJoinPointSpecification, result)) {
            Level level = Level.parse(loggableJoinPointSpecification.getAnnotation().level());
            Object[] params = {
                    loggableJoinPointSpecification.getClassName(),
                    loggableJoinPointSpecification.getMethodName(),
                    LoggableType.METHOD_RESULT.getPrefix(),
                    getVerifiedLogResult(result)
            };
            log.log(level, LOG_PATTERN, params);
        }
    }

    private boolean isResponseEntityErrorNotLogged(LoggableJoinPointSpecification loggableJoinPointSpecification, Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            if (responseEntity.getStatusCode().isError()) {
                Object[] params = {
                        loggableJoinPointSpecification.getClassName(),
                        loggableJoinPointSpecification.getMethodName(),
                        LoggableType.METHOD_EXCEPTION.getPrefix(),
                        getVerifiedLogResult(responseEntity)
                };
                log.log(SEVERE, LOG_PATTERN, params);
                return false;
            }
        }
        return true;
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "throwable")
    public void onFailed(JoinPoint joinPoint, Throwable throwable) {
        LoggableJoinPointSpecification loggableJoinPointSpecification = createLoggableJoinPointSpecification(joinPoint);
        if (isContainingLoggableType(loggableJoinPointSpecification, LoggableType.METHOD_EXCEPTION)) {
            logException(loggableJoinPointSpecification, throwable);
        }
    }

    private void logException(LoggableJoinPointSpecification loggableJoinPointSpecification, Throwable throwable) {
        Object[] params = {loggableJoinPointSpecification.getClassName(),
                loggableJoinPointSpecification.getMethodName(),
                LoggableType.METHOD_EXCEPTION.getPrefix(),
                getVerifiedLogResult(throwable)};
        log.log(SEVERE, LOG_PATTERN, params);
    }

    @SneakyThrows
    @Around(POINTCUT)
    public Object onDuration(ProceedingJoinPoint joinPoint) {
        long startTimeMillis = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTimeMillis;
        LoggableJoinPointSpecification loggableJoinPointSpecification = createLoggableJoinPointSpecification(joinPoint);
        if (isContainingLoggableType(loggableJoinPointSpecification, LoggableType.METHOD_DURATION)) {
            logDuration(loggableJoinPointSpecification, duration);
        }
        return proceed;
    }

    private void logDuration(LoggableJoinPointSpecification loggableJoinPointSpecification, long duration) {
        Level level = Level.parse(loggableJoinPointSpecification.getAnnotation().level());
        Object[] params = {loggableJoinPointSpecification.getClassName(),
                loggableJoinPointSpecification.getMethodName(),
                LoggableType.METHOD_DURATION.getPrefix(),
                getVerifiedLogResult(String.format("%d ms", duration))};
        log.log(level, LOG_PATTERN, params);
    }

    private boolean isContainingLoggableType(LoggableJoinPointSpecification loggableJoinPointSpecification, LoggableType loggableType) {
        return asList(loggableJoinPointSpecification.getAnnotation().value())
                .contains(loggableType);
    }

    private String getVerifiedLogResult(Object result) {
        return Optional.ofNullable(result)
                .map(Object::toString)
                .filter(StringUtils::hasLength)
                .orElse(NOTHING_TO_LOG);
    }

    private LoggableJoinPointSpecification createLoggableJoinPointSpecification(JoinPoint joinPoint) {
        return new LoggableJoinPointSpecification(joinPoint, Loggable.class);
    }
}