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
        throw throwException();
    }

    @Loggable({})
    public void throwExceptionWithAnnotationWithoutLoggableType() {
        throw throwException();
    }

    public void throwExceptionWithoutAnnotation() {
        throw throwException();
    }

    private RuntimeException throwException() {
        return new RuntimeException(MESSAGE);
    }
}
