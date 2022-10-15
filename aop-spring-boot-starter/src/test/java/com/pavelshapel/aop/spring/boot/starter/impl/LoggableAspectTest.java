package com.pavelshapel.aop.spring.boot.starter.impl;

import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.AspectTester;
import com.pavelshapel.aop.spring.boot.starter.model.LoggableType;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(classes = {
        AopStarterAutoConfiguration.class,
        AspectTester.class
})
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
@ExtendWith(OutputCaptureExtension.class)
class LoggableAspectTest {
    @SpyBean
    private LoggableAspect loggableAspect;
    @Autowired
    private AspectTester aspectTester;

    @Test
    void call_WithLoggableAnnotationWithLoggableType_ShouldLogResult(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithLoggableAnnotationWithLoggableType();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_RESULT.getPrefix(), message);
        verify(loggableAspect).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithLoggableAnnotationWithoutLoggableType_ShouldNotLogResult(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithLoggableAnnotationWithoutLoggableType();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_RESULT.getPrefix())
                .doesNotContain(message);
        verify(loggableAspect).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutLoggableAnnotation_ShouldNotLogResult(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithoutLoggableAnnotation();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_RESULT.getPrefix())
                .doesNotContain(message);
        verifyNoInteractions(loggableAspect);
    }

    @Test
    void call_WithLoggableAnnotationWithLoggableType_ShouldLogException(CapturedOutput capturedOutput) {
        assertThatThrownBy(() -> aspectTester.throwExceptionWithLoggableAnnotationWithLoggableType())
                .isInstanceOf(RuntimeException.class);
        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_EXCEPTION.getPrefix())
                .contains(RuntimeException.class.getName());
        verify(loggableAspect).onFailed(any(JoinPoint.class), any());
    }

    @Test
    void call_WithLoggableAnnotationWithoutLoggableType_ShouldNotLogException(CapturedOutput capturedOutput) {
        assertThatThrownBy(() -> aspectTester.throwExceptionWithLoggableAnnotationWithoutLoggableType())
                .isInstanceOf(RuntimeException.class);
        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_EXCEPTION.getPrefix());
        verify(loggableAspect).onFailed(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutLoggableAnnotation_ShouldNotLogException(CapturedOutput capturedOutput) {
        assertThatThrownBy(() -> aspectTester.throwExceptionWithoutAnnotation())
                .isInstanceOf(RuntimeException.class);
        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_EXCEPTION.getPrefix());
        verifyNoInteractions(loggableAspect);
    }

    @Test
    void call_WithLoggableAnnotationWithLoggableType_ShouldLogDuration(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithLoggableAnnotationWithLoggableType();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_DURATION.getPrefix())
                .contains(message);
        verify(loggableAspect).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithLoggableAnnotationWithoutLoggableType_ShouldNotLogDuration(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithLoggableAnnotationWithoutLoggableType();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_DURATION.getPrefix(), message)
                .doesNotContain(message);
        verify(loggableAspect).onSuccess(any(JoinPoint.class), any());
    }

    @Test
    void call_WithoutLoggableAnnotation_ShouldNotLogDuration(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithoutLoggableAnnotation();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .doesNotContain(LoggableType.METHOD_DURATION.getPrefix())
                .doesNotContain(message);
        verifyNoInteractions(loggableAspect);
    }
}