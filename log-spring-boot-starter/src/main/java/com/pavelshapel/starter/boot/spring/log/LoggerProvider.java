package com.pavelshapel.starter.boot.spring.log;

import static java.lang.System.currentTimeMillis;

import java.util.function.Supplier;
import org.slf4j.Logger;

public interface LoggerProvider {
  Logger getLogger();

  default <R> R executeAndLog(String message, Supplier<R> supplier) {
    getLogger().info("[→] {}...", message);
    long startTime = currentTimeMillis();
    R result = supplier.get();
    long endTime = currentTimeMillis();
    long executionTime = endTime - startTime;
    getLogger().info("[←] completed in [{}] ms. {}", executionTime, message);
    return result;
  }

  default void executeAndLog(String message, Runnable runnable) {
    executeAndLog(
        message,
        () -> {
          runnable.run();
          return Void.TYPE;
        });
  }
}
