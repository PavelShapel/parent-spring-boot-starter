package com.pavelshapel.starter.boot.spring.log;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.function.Supplier;
import org.slf4j.Logger;

public interface LoggerProvider {
  Logger logger();

  default <R> R executeAndLog(String message, Supplier<R> supplier) {
    logger().info("[→] {}...", message);
    long startTime = nanoTime();
    R result = supplier.get();
    long endTime = nanoTime();
    long executionTime = NANOSECONDS.toMillis(endTime - startTime);
    logger().info("[←] completed in [{}] ms. {}", executionTime, message);
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
