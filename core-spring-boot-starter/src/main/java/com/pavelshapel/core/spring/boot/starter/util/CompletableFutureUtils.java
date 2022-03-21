package com.pavelshapel.core.spring.boot.starter.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompletableFutureUtils {
    <T> CompletableFuture<List<T>> allOf(Collection<CompletableFuture<T>> completableFutures);

    <T> CompletableFuture<T> anyOf(Collection<CompletableFuture<T>> completableFutures);
}
