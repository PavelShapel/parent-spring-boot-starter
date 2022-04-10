package com.pavelshapel.aop.spring.boot.starter.log.method;

import org.springframework.stereotype.Component;

@Component
public class AspectTester {
    public static final String MESSAGE = "test message";

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    public String sendMessageWithAnnotationWithLoggableType() {
        return MESSAGE;
    }

    @Loggable({})
    public String sendMessageWithAnnotationWithoutLoggableType() {
        return MESSAGE;
    }

    public String sendMessageWithoutAnnotation() {
        return MESSAGE;
    }

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    public void throwExceptionWithAnnotationWithLoggableType() {
        throwRuntimeException();
    }

    @Loggable({})
    public void throwExceptionWithAnnotationWithoutLoggableType() {
        throwRuntimeException();
    }

    public void throwExceptionWithoutAnnotation() {
        throwRuntimeException();
    }

    private void throwRuntimeException() {
        throw new RuntimeException(MESSAGE);
    }
}
