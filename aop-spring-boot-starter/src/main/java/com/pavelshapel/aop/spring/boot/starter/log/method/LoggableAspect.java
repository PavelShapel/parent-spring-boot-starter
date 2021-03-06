package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Log4j2
public class LoggableAspect {
    private static final String LOG_PATTERN = "[{}.{}] {} -> {}";
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
        LoggableMethodSpecification loggableMethodSpecification = new LoggableMethodSpecification(joinPoint);
        if (isContainingLoggableType(loggableMethodSpecification, LoggableType.METHOD_RESULT)) {
            logSuccess(loggableMethodSpecification, result);
        }
    }

    private void logSuccess(LoggableMethodSpecification loggableMethodSpecification, Object result) {
        if (isResponseEntityErrorNotLogged(loggableMethodSpecification, result)) {
            Level level = Level.toLevel(loggableMethodSpecification.getLoggable().level().toString());
            log.log(level,
                    LOG_PATTERN,
                    loggableMethodSpecification.getMethodDeclaringClassName(),
                    loggableMethodSpecification.getMethodName(),
                    LoggableType.METHOD_RESULT.getPrefix(),
                    getVerifiedLogResult(result)
            );
        }
    }

    private boolean isResponseEntityErrorNotLogged(LoggableMethodSpecification loggableMethodSpecification, Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            if (responseEntity.getStatusCode().isError()) {
                log.error(LOG_PATTERN,
                        loggableMethodSpecification.getMethodDeclaringClassName(),
                        loggableMethodSpecification.getMethodName(),
                        LoggableType.METHOD_EXCEPTION.getPrefix(),
                        getVerifiedLogResult(responseEntity));
                return false;
            }
        }
        return true;
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "throwable")
    public void onFailed(JoinPoint joinPoint, Throwable throwable) {
        LoggableMethodSpecification loggableMethodSpecification = new LoggableMethodSpecification(joinPoint);
        if (isContainingLoggableType(loggableMethodSpecification, LoggableType.METHOD_EXCEPTION)) {
            logException(loggableMethodSpecification, throwable);
        }
    }

    private void logException(LoggableMethodSpecification loggableMethodSpecification, Throwable throwable) {
        log.error(
                LOG_PATTERN,
                loggableMethodSpecification.getMethodDeclaringClassName(),
                loggableMethodSpecification.getMethodName(),
                LoggableType.METHOD_EXCEPTION.getPrefix(),
                getVerifiedLogResult(throwable)
        );
    }

    @SneakyThrows
    @Around(POINTCUT)
    public Object onDuration(ProceedingJoinPoint joinPoint) {
        long startTimeMillis = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTimeMillis;
        LoggableMethodSpecification loggableMethodSpecification = new LoggableMethodSpecification(joinPoint);
        if (isContainingLoggableType(loggableMethodSpecification, LoggableType.METHOD_DURATION)) {
            logDuration(loggableMethodSpecification, duration);
        }
        return proceed;
    }

    private void logDuration(LoggableMethodSpecification loggableMethodSpecification, long duration) {
        Level level = Level.toLevel(loggableMethodSpecification.getLoggable().level().toString());
        log.log(level,
                LOG_PATTERN,
                loggableMethodSpecification.getMethodDeclaringClassName(),
                loggableMethodSpecification.getMethodName(),
                LoggableType.METHOD_DURATION.getPrefix(),
                getVerifiedLogResult(String.format("%d ms", duration))
        );
    }

    private boolean isContainingLoggableType(LoggableMethodSpecification loggableMethodSpecification, LoggableType loggableType) {
        return Arrays.asList(loggableMethodSpecification.getLoggable().value())
                .contains(loggableType);
    }

    private String getVerifiedLogResult(Object result) {
        return Objects.isNull(result) || result.toString().isEmpty() ? NOTHING_TO_LOG : result.toString();
    }
}