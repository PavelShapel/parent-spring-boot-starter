package com.pavelshapel.log.spring.boot.starter;

import org.slf4j.Logger;

import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;

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
}