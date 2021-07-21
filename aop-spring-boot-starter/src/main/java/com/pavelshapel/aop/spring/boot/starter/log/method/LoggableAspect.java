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
    private static final String LOG_PATTERN = "[{}.{}] {}: {}";
    private static final String NOTHING_TO_LOG = "nothing to log";
    private static final String POINTCUT = "execution(* *(..)) && (annotatedMethod() || annotatedClass())";
    private MethodSpecification methodSpecification;

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
        initializeMethodSpecification(joinPoint);
        if (isContainingLoggableType(LoggableType.METHOD_RESULT)) {
            logSuccess(result);
        }
    }

    private void logSuccess(Object result) {
        if (isResponseEntityErrorNotLogged(result)) {
            Level level = Level.toLevel(methodSpecification.getLoggable().level().toString());
            log.log(level,
                    LOG_PATTERN,
                    methodSpecification.getMethodDeclaringClassName(),
                    methodSpecification.getMethodName(),
                    LoggableType.METHOD_RESULT.getPrefix(),
                    getVerifiedLogResult(result)
            );
        }
    }

    private boolean isResponseEntityErrorNotLogged(Object result) {
        if (result instanceof ResponseEntity) {
            final ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            if (responseEntity.getStatusCode().isError()) {
                log.error(LOG_PATTERN,
                        methodSpecification.getMethodDeclaringClassName(),
                        methodSpecification.getMethodName(),
                        LoggableType.METHOD_EXCEPTION.getPrefix(),
                        getVerifiedLogResult(responseEntity));
                return false;
            }
        }
        return true;
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "throwable")
    public void onFailed(JoinPoint joinPoint, Throwable throwable) {
        initializeMethodSpecification(joinPoint);
        if (isContainingLoggableType(LoggableType.METHOD_EXCEPTION)) {
            logException(throwable);
        }
    }

    private void logException(Throwable throwable) {
        log.error(
                LOG_PATTERN,
                methodSpecification.getMethodDeclaringClassName(),
                methodSpecification.getMethodName(),
                LoggableType.METHOD_EXCEPTION.getPrefix(),
                getVerifiedLogResult(throwable.getMessage())
        );
    }

    @SneakyThrows
    @Around(POINTCUT)
    public Object onDuration(ProceedingJoinPoint joinPoint) {
        long startTimeMillis = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTimeMillis;
        initializeMethodSpecification(joinPoint);
        if (isContainingLoggableType(LoggableType.METHOD_DURATION)) {
            logDuration(duration);
        }
        return proceed;
    }

    private void logDuration(long duration) {
        Level level = Level.toLevel(methodSpecification.getLoggable().level().toString());
        log.log(level,
                LOG_PATTERN,
                methodSpecification.getMethodDeclaringClassName(),
                methodSpecification.getMethodName(),
                LoggableType.METHOD_DURATION.getPrefix(),
                getVerifiedLogResult(String.format("%d ms", duration))
        );
    }

    private void initializeMethodSpecification(JoinPoint joinPoint) {
        this.methodSpecification = new MethodSpecification(joinPoint);
    }

    private boolean isContainingLoggableType(LoggableType loggableType) {
        return Arrays.asList(methodSpecification.getLoggable().value())
                .contains(loggableType);
    }

    private String getVerifiedLogResult(Object result) {
        return Objects.isNull(result) || result.toString().isEmpty() ? NOTHING_TO_LOG : result.toString();
    }
}