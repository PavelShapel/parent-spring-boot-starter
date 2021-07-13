package com.pavelshapel.aop.spring.boot.starter.log.method.result;

import com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog;
import com.pavelshapel.aop.spring.boot.starter.log.method.MethodSpecification;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;

@Aspect
@Log4j2
public class MethodResultAspectLog extends AbstractAspectLog {
    private MethodSpecification methodSpecification;

    @Pointcut("@annotation(LogMethodResult)")
    public void callLogMethodResult() {
        //pointcut
    }

    @AfterReturning(pointcut = "callLogMethodResult()", returning = "result")
    public void onSuccess(JoinPoint joinPoint, Object result) {
        initializeMethodSpecification(joinPoint);
        logSuccess(result);
    }

    @AfterThrowing(pointcut = "callLogMethodResult()", throwing = "throwable")
    public void onFailed(JoinPoint joinPoint, Throwable throwable) {
        initializeMethodSpecification(joinPoint);
        logException(throwable);
    }

    private void initializeMethodSpecification(JoinPoint joinPoint) {
        this.methodSpecification = new MethodSpecification(joinPoint, LogMethodResult.class);
    }

    private void logSuccess(Object result) {
        if (isResponseEntityErrorNotLogged(result)) {
            final Level level = Level.toLevel(methodSpecification.getLogSpecification().level().toString());
            log.log(level,
                    LOG_PATTERN,
                    methodSpecification.getMethodDeclaringClassName(),
                    methodSpecification.getMethodName(),
                    methodSpecification.getLogSpecification().successPrefix(),
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
                        methodSpecification.getLogSpecification().exceptionPrefix(),
                        getVerifiedLogResult(responseEntity));
                return false;
            }
        }
        return true;
    }

    private void logException(Throwable throwable) {
        log.error(
                LOG_PATTERN,
                methodSpecification.getMethodDeclaringClassName(),
                methodSpecification.getMethodName(),
                methodSpecification.getLogSpecification().exceptionPrefix(),
                getVerifiedLogResult(throwable.getMessage())
        );
    }
}