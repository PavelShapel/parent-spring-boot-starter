package com.pavelshapel.starter.boot.spring.log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.lang.reflect.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InjectionPoint;

@ExtendWith(MockitoExtension.class)
final class LogStarterAutoConfigurationTest {
  @Mock private InjectionPoint injectionPoint;

  @Mock private Member member;

  private LogStarterAutoConfiguration sut;

  @BeforeEach
  void setUp() {
    sut = new LogStarterAutoConfiguration();
  }

  @Test
  void loggerReturnsLoggerForDeclaringClassWhenMemberPresent() {
    when(injectionPoint.getMember()).thenReturn(member);
    doReturn(LogStarterAutoConfigurationTest.class).when(member).getDeclaringClass();

    Logger result = sut.logger(injectionPoint);

    assertThat(result.getName()).isEqualTo(LogStarterAutoConfigurationTest.class.getName());
  }

  @Test
  void loggerThrowsWhenMemberIsNull() {
    when(injectionPoint.getMember()).thenReturn(null);

    assertThatThrownBy(() -> sut.logger(injectionPoint))
        .isInstanceOf(BeanInitializationException.class)
        .hasMessage("Could not initialize logger");
  }
}
