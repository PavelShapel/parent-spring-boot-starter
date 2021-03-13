package com.pavelshapel.aop.spring.boot.starter.log.method.duration;

import com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.log.TestMessenger;
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
        PREFIX + ".log-method-duration=" + TRUE
})
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        TestMessenger.class
})
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
@ExtendWith(MockitoExtension.class)
class MethodDurationAspectLogTest {
    @SpyBean
    private MethodDurationAspectLog methodDurationAspectLog;
    @Autowired
    private TestMessenger testMessenger;

    @Test
    void call_WithAnnotation_ShouldLogResult() {
        testMessenger.sendMessageWithAspect();

        verify(methodDurationAspectLog, times(1)).callLogMethodDuration(any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogResult() {
        testMessenger.sendMessageWithoutAspect();

        verifyNoInteractions(methodDurationAspectLog);
    }

    @Test
    void call_WithAnnotation_ShouldNotLogException() {
        Assertions.assertThrows(RuntimeException.class, () -> testMessenger.throwExceptionWithAspect());
        verify(methodDurationAspectLog, times(1)).callLogMethodDuration(any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogException() {
        Assertions.assertThrows(RuntimeException.class, () -> testMessenger.throwExceptionWithoutAspect());
        verifyNoInteractions(methodDurationAspectLog);
    }
}