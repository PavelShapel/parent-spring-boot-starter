package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.logging.Level;

import static java.util.Arrays.asList;
import static java.util.logging.Level.SEVERE;

@Aspect
@Log
public class LoggableAspect {
    private static final String LOG_PATTERN = "[{0}.{1}] {2} -> {3}";
    private static final String NOTHING_TO_LOG = "nothing to log";
    private static final String POINTCUT = "execution(* *(..)) && (annotatedMethod() || annotatedClass())";

    @Pointcut("@annotation(com.pavelshapel.aop.spring.boot.starter.log.method.Loggable)")
    public void annotatedMethod() {
        //pointcut
    }

    @Pointcut("@within(com.pavelshapel.aop.spring.boot.starter.log.method.Loggable)")
    public void annotatedClass() {
        //pointcut
    }

    @AfterReturning(pointcut = POINTCUT, returning = "result")
    public void onSuccess(JoinPoint joinPoint, Object result) {
        LoggableJoinPointSpecification loggableJoinPointSpecification = new LoggableJoinPointSpecification(joinPoint);
        if (isContainingLoggableType(loggableJoinPointSpecification, LoggableType.METHOD_RESULT)) {
            logSuccess(loggableJoinPointSpecification, result);
        }
    }

    private void logSuccess(LoggableJoinPointSpecification loggableJoinPointSpecification, Object result) {
        if (isResponseEntityErrorNotLogged(loggableJoinPointSpecification, result)) {
            Level level = Level.parse(loggableJoinPointSpecification.getLoggable().level());
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
        LoggableJoinPointSpecification loggableJoinPointSpecification = new LoggableJoinPointSpecification(joinPoint);
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
        LoggableJoinPointSpecification loggableJoinPointSpecification = new LoggableJoinPointSpecification(joinPoint);
        if (isContainingLoggableType(loggableJoinPointSpecification, LoggableType.METHOD_DURATION)) {
            logDuration(loggableJoinPointSpecification, duration);
        }
        return proceed;
    }

    private void logDuration(LoggableJoinPointSpecification loggableJoinPointSpecification, long duration) {
        Level level = Level.parse(loggableJoinPointSpecification.getLoggable().level());
        Object[] params = {loggableJoinPointSpecification.getClassName(),
                loggableJoinPointSpecification.getMethodName(),
                LoggableType.METHOD_DURATION.getPrefix(),
                getVerifiedLogResult(new StringBuilder(String.valueOf(duration)).append(" ms"))};
        log.log(level, LOG_PATTERN, params);
    }

    private boolean isContainingLoggableType(LoggableJoinPointSpecification loggableJoinPointSpecification, LoggableType loggableType) {
        return asList(loggableJoinPointSpecification.getLoggable().value())
                .contains(loggableType);
    }

    private String getVerifiedLogResult(Object result) {
        return Optional.ofNullable(result)
                .map(Object::toString)
                .filter(StringUtils::hasLength)
                .orElse(NOTHING_TO_LOG);
    }
}