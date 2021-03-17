package com.pavelshapel.aop.spring.boot.starter.log;

import com.pavelshapel.aop.spring.boot.starter.log.method.duration.LogMethodDuration;
import com.pavelshapel.aop.spring.boot.starter.log.method.result.LogMethodResult;
import org.springframework.stereotype.Component;

@Component
public class TestMessenger {
    public static final String MESSAGE = "test message";

    @LogMethodResult
    @LogMethodDuration
    public String sendMessageWithAspect() {
        return MESSAGE;
    }

    public String sendMessageWithoutAspect() {
        return MESSAGE;
    }

    @LogMethodResult
    @LogMethodDuration
    public void throwExceptionWithAspect() {
        throw new RuntimeException(MESSAGE);
    }

    public void throwExceptionWithoutAspect() {
        throw new RuntimeException(MESSAGE);
    }
}
