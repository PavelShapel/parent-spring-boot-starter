package com.pavelshapel.starter.boot.spring.log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
final class LoggerProviderTest {
  @Mock private Logger logger;

  private LoggerProvider sut;

  @BeforeEach
  void setUp() {
    sut = () -> logger;
  }

  @Test
  void executeAndLogSupplierReturnsResultAndLogsExecution() {
    String message = "Test DURATION log";
    String expected = "result";

    String result = sut.executeAndLog(message, () -> expected);

    assertThat(result).isEqualTo(expected);
    InOrder inOrder = inOrder(logger);
    inOrder.verify(logger).info("[→] {}...", message);
    inOrder.verify(logger).info(eq("[←] completed in [{}] ms. {}"), anyLong(), eq(message));
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  void executeAndLogRunnableExecutesRunnableAndLogsExecution() {
    String message = "Test Runnable execution";
    Set<String> executedMessages = new HashSet<>();
    Runnable runnable = () -> executedMessages.add(message);

    sut.executeAndLog(message, runnable);

    assertThat(executedMessages).containsExactly(message);
    InOrder inOrder = inOrder(logger);
    inOrder.verify(logger).info("[→] {}...", message);
    inOrder.verify(logger).info(eq("[←] completed in [{}] ms. {}"), anyLong(), eq(message));
    inOrder.verifyNoMoreInteractions();
  }
}
