package com.pavelshapel.aop.spring.boot.starter.log.method.result;

import com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.log.TestMessenger;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration.PREFIX;
import static com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        PREFIX + ".log-method-result="+TRUE
})
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        TestMessenger.class
})
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
@ExtendWith(MockitoExtension.class)
class MethodResultAspectLogTest {
    @SpyBean
    private MethodResultAspectLog methodResultAspectLog;
    @Autowired
    private TestMessenger testMessenger;

    @Test
    void call_WithAnnotation_ShouldLogResult() {
        testMessenger.sendMessageWithAspect();

        verify(methodResultAspectLog, times(1)).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogResult() {
        testMessenger.sendMessageWithoutAspect();

        verifyNoInteractions(methodResultAspectLog);
    }

    @Test
    void call_WithAnnotation_ShouldLogException() {
        Assertions.assertThrows(RuntimeException.class, () -> testMessenger.throwExceptionWithAspect());
        verify(methodResultAspectLog, times(1)).onFailed(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogException() {
        Assertions.assertThrows(RuntimeException.class, () -> testMessenger.throwExceptionWithoutAspect());
        verifyNoInteractions(methodResultAspectLog);
    }
}