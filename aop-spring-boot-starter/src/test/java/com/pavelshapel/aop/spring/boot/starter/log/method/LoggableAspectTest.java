//package com.pavelshapel.aop.spring.boot.starter.log.method;
//
//import com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration;
//import org.aspectj.lang.JoinPoint;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.boot.test.system.CapturedOutput;
//import org.springframework.boot.test.system.OutputCaptureExtension;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ContextConfiguration;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ContextConfiguration(classes = {
//        StarterAutoConfiguration.class,
//        AspectTester.class
//})
//@Import(AnnotationAwareAspectJAutoProxyCreator.class)
//@ExtendWith(OutputCaptureExtension.class)
//class LoggableAspectTest {
//    @SpyBean
//    private LoggableAspect loggableAspect;
//    @Autowired
//    private AspectTester aspectTester;
//
//    @Test
//    void initialization() {
//        assertThat(aspectTester).isNotNull();
//        assertThat(loggableAspect).isNotNull();
//    }
//
//    @Test
//    void call_WithAnnotationWithLoggableType_ShouldLogResult(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithAnnotationWithLoggableType();
//
//        assertThat(capturedOutput.getOut())
//                .contains(LoggableType.METHOD_RESULT.getPrefix(), AspectTester.MESSAGE);
//        verify(loggableAspect, times(1))
//                .onSuccess(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithAnnotationWithoutLoggableType_ShouldNotLogResult(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithAnnotationWithoutLoggableType();
//
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_RESULT.getPrefix());
//        verify(loggableAspect, times(1))
//                .onSuccess(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithoutAnnotation_ShouldNotLogResult(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithoutAnnotation();
//
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_RESULT.getPrefix());
//        verifyNoInteractions(loggableAspect);
//    }
//
//    @Test
//    void call_WithAnnotationWithLoggableType_ShouldLogException(CapturedOutput capturedOutput) {
//        assertThatThrownBy(() -> aspectTester.throwExceptionWithAnnotationWithLoggableType())
//                .isInstanceOf(RuntimeException.class);
//        assertThat(capturedOutput.getOut())
//                .contains(LoggableType.METHOD_EXCEPTION.getPrefix());
//        verify(loggableAspect, times(1))
//                .onFailed(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithAnnotationWithoutLoggableType_ShouldNotLogException(CapturedOutput capturedOutput) {
//        assertThatThrownBy(() -> aspectTester.throwExceptionWithAnnotationWithoutLoggableType())
//                .isInstanceOf(RuntimeException.class);
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_EXCEPTION.getPrefix());
//        verify(loggableAspect, times(1))
//                .onFailed(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithoutAnnotation_ShouldNotLogException(CapturedOutput capturedOutput) {
//        assertThatThrownBy(() -> aspectTester.throwExceptionWithoutAnnotation())
//                .isInstanceOf(RuntimeException.class);
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_EXCEPTION.getPrefix());
//        verifyNoInteractions(loggableAspect);
//    }
//
//    @Test
//    void call_WithAnnotationWithLoggableType_ShouldLogDuration(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithAnnotationWithLoggableType();
//
//        assertThat(capturedOutput.getOut())
//                .contains(LoggableType.METHOD_DURATION.getPrefix(), AspectTester.MESSAGE);
//        verify(loggableAspect, times(1))
//                .onSuccess(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithAnnotationWithoutLoggableType_ShouldNotLogDuration(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithAnnotationWithoutLoggableType();
//
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_DURATION.getPrefix());
//        verify(loggableAspect, times(1))
//                .onSuccess(any(JoinPoint.class), any());
//    }
//
//    @Test
//    void call_WithoutAnnotation_ShouldNotLogDuration(CapturedOutput capturedOutput) {
//        aspectTester.sendMessageWithoutAnnotation();
//
//        assertThat(capturedOutput.getOut())
//                .doesNotContain(LoggableType.METHOD_DURATION.getPrefix());
//        verifyNoInteractions(loggableAspect);
//    }
//}