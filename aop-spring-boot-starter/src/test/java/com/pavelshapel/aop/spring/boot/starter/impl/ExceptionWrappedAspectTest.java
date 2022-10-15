package com.pavelshapel.aop.spring.boot.starter.impl;

import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.aop.spring.boot.starter.AspectTester;
import com.pavelshapel.aop.spring.boot.starter.model.LoggableType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

import static com.pavelshapel.aop.spring.boot.starter.AspectTester.MESSAGE;
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
class ExceptionWrappedAspectTest {
    @SpyBean
    private LoggableAspect loggableAspect;
    @SpyBean
    private ExceptionWrappedAspect exceptionWrappedAspect;
    @Autowired
    private AspectTester aspectTester;

    @Test
    void call_WithLoggableAnnotationOnlyAndThrowException_ShouldNotReplaceAndLogException(CapturedOutput capturedOutput) {
        assertThatThrownBy(() -> aspectTester.throwExceptionWithLoggableAnnotationWithLoggableType())
                .isInstanceOf(RuntimeException.class);
        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_EXCEPTION.getPrefix())
                .contains(MESSAGE)
                .contains(RuntimeException.class.getName());
        verifyNoInteractions(exceptionWrappedAspect);
    }
    @Test
    void call_WithLoggableAndExceptionWrappedAnnotationsAndThrowException_ShouldReplaceAndLogException(CapturedOutput capturedOutput) {
        assertThatThrownBy(() -> aspectTester.throwExceptionWithLoggableAndExceptionWrappedAnnotations(MESSAGE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE);
        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_EXCEPTION.getPrefix())
                .contains(MESSAGE)
                .contains(IllegalArgumentException.class.getName());
        verify(exceptionWrappedAspect).onDuration(any(ProceedingJoinPoint.class));
    }

    @Test
    void call_WithExceptionWrappedAnnotationAndNotThrowException_ShouldNotReplaceException(CapturedOutput capturedOutput) {
        String message = aspectTester.sendMessageWithExceptionWrappedAnnotation();

        assertThat(capturedOutput)
                .extracting(CapturedOutput::getOut)
                .asString()
                .contains(LoggableType.METHOD_RESULT.getPrefix())
                .contains(message)
                .doesNotContain(IllegalArgumentException.class.getName());
        verify(exceptionWrappedAspect).onDuration(any(ProceedingJoinPoint.class));
    }
}