package com.pavelshapel.aop.spring.boot.starter.log.method.result;

import com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.log.AspectTester;
import org.aspectj.lang.JoinPoint;
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

import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.SUCCESS_RESULT;
import static com.pavelshapel.aop.spring.boot.starter.log.AbstractAspectLog.THREW_AN_EXCEPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        AspectTester.class
})
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
@ExtendWith(OutputCaptureExtension.class)
class MethodResultAspectLogTest {
    @SpyBean
    private MethodResultAspectLog methodResultAspectLog;
    @Autowired
    private AspectTester aspectTester;

    @Test
    void initialization() {
        assertThat(aspectTester).isNotNull();
        assertThat(methodResultAspectLog).isNotNull();
    }

    @Test
    void call_WithAnnotation_ShouldLogResult(CapturedOutput capturedOutput) {
        aspectTester.sendMessageWithAspect();

        assertThat(capturedOutput.getOut()).contains(SUCCESS_RESULT, AspectTester.MESSAGE);
        verify(methodResultAspectLog, times(1)).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogResult(CapturedOutput capturedOutput) {
        aspectTester.sendMessageWithoutAspect();

        assertThat(capturedOutput.getOut()).doesNotContain(SUCCESS_RESULT);
        verifyNoInteractions(methodResultAspectLog);
    }

    @Test
    void call_WithAnnotation_ShouldLogException(CapturedOutput capturedOutput) {
        Assertions.assertThrows(RuntimeException.class, () -> aspectTester.throwExceptionWithAspect());
        assertThat(capturedOutput.getOut()).contains(THREW_AN_EXCEPTION);
        verify(methodResultAspectLog, times(1)).onFailed(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutAnnotation_ShouldNotLogException(CapturedOutput capturedOutput) {
        Assertions.assertThrows(RuntimeException.class, () -> aspectTester.throwExceptionWithoutAspect());
        assertThat(capturedOutput.getOut()).doesNotContain(THREW_AN_EXCEPTION);
        verifyNoInteractions(methodResultAspectLog);
    }
}