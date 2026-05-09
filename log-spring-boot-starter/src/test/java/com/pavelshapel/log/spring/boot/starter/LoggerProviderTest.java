package com.pavelshapel.log.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class LoggerProviderTest {
  @Mock private Logger mockLogger;

  private UnderTest underTest;

  private record UnderTest(Logger logger) implements LoggerProvider {
    String executeAndLog(String message) {
      return executeAndLog(message, () -> "result");
    }

    @Override
    public Logger getLogger() {
      return logger;
    }
  }

  @BeforeEach
  void setUp() {
    underTest = new UnderTest(mockLogger);
  }

  @Test
  void shouldReturnValidResult() {
    String message = "Test DURATION log";

    String result = underTest.executeAndLog(message);

    assertThat(result).isEqualTo("result");
  }

  @Test
  void shouldInvokeLoggerTwice() {
    String message = "Test DURATION log";
    underTest.executeAndLog(message);

    InOrder inOrder = inOrder(mockLogger);

    inOrder.verify(mockLogger).info("[→] {}...", "Test DURATION log");
    inOrder
        .verify(mockLogger)
        .info(eq("[←] completed in [{}] ms. {}"), anyLong(), eq("Test DURATION log"));
    inOrder.verifyNoMoreInteractions();
  }
}
