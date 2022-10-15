package com.pavelshapel.aop.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aop.spring.boot.starter.annotation.Loggable;
import com.pavelshapel.aop.spring.boot.starter.model.LoggableType;
import org.springframework.stereotype.Component;

@Component
public class AspectTester {
    public static final String MESSAGE = "test message";

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    public String sendMessageWithLoggableAnnotationWithLoggableType() {
        return MESSAGE;
    }

    @Loggable({})
    public String sendMessageWithLoggableAnnotationWithoutLoggableType() {
        return MESSAGE;
    }

    public String sendMessageWithoutLoggableAnnotation() {
        return MESSAGE;
    }

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    public void throwExceptionWithLoggableAnnotationWithLoggableType() {
        throwRuntimeException();
    }

    @Loggable({})
    public void throwExceptionWithLoggableAnnotationWithoutLoggableType() {
        throwRuntimeException();
    }

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    @ExceptionWrapped
    public void throwExceptionWithLoggableAndExceptionWrappedAnnotations(String message) {
        throwRuntimeException(message);
    }

    @Loggable({LoggableType.METHOD_RESULT, LoggableType.METHOD_DURATION, LoggableType.METHOD_EXCEPTION})
    @ExceptionWrapped
    public String sendMessageWithExceptionWrappedAnnotation() {
        return MESSAGE;
    }

    public void throwExceptionWithoutAnnotation() {
        throwRuntimeException();
    }

    private void throwRuntimeException() {
        throwRuntimeException(MESSAGE);
    }

    private void throwRuntimeException(String message) {
        throw new RuntimeException(message);
    }
}
