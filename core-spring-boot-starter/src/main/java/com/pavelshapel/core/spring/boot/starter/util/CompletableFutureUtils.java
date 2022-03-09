package com.pavelshapel.core.spring.boot.starter.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureUtils {
    public <T> CompletableFuture<List<T>> allOf(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(unused -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    public <T> CompletableFuture<T> anyOf(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(object -> (T) object);
    }
}