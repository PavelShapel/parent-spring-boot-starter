package com.pavelshapel.aop.spring.boot.starter.log.method.duration;

import com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.log.TestMessenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.SUCCESS_DURATION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        TestMessenger.class
})
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
@ExtendWith(OutputCaptureExtension.class)
class MethodDurationAspectLogTest {
    @SpyBean
    private MethodDurationAspectLog methodDurationAspectLog;
    @Autowired
    private TestMessenger testMessenger;

    @Test
    void call_WithAnnotation_ShouldLogDuration(CapturedOutput capturedOutput) {
        testMessenger.sendMessageWithAspect();

        assertThat(capturedOutput.getOut()).contains(SUCCESS_DURATION);
        verify(methodDurationAspectLog, times(1)).callLogMethodDuration(any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogDuration(CapturedOutput capturedOutput) {
        testMessenger.sendMessageWithoutAspect();

        assertThat(capturedOutput.getOut()).doesNotContain(SUCCESS_DURATION);
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