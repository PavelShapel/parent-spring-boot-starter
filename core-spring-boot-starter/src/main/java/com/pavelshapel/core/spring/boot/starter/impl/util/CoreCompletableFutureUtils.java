package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.CompletableFutureUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CoreCompletableFutureUtils implements CompletableFutureUtils {
    @Override
    public <T> CompletableFuture<List<T>> allOf(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(unused -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .toList()
                );
    }

    @Override
    public <T> CompletableFuture<T> anyOf(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(object -> (T) object);
    }
}