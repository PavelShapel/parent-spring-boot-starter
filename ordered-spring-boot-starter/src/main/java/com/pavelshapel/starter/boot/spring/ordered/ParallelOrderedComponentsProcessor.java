package com.pavelshapel.starter.boot.spring.ordered;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ParallelOrderedComponentsProcessor<
        PAYLOAD, RESULT, COMPONENT extends OrderedComponent<PAYLOAD, RESULT>>
    extends OrderedComponentsProcessor<PAYLOAD, RESULT, COMPONENT> {
  private final Executor executor;

  protected ParallelOrderedComponentsProcessor(List<COMPONENT> components) {
    super(components);
    this.executor = Executors.newVirtualThreadPerTaskExecutor();
  }

  @Override
  protected final List<OrderedResult<RESULT>> apply(PAYLOAD payload) {
    return getApplicableComponentsStream(payload)
        .map(component -> CompletableFuture.supplyAsync(() -> component.apply(payload), executor))
        .toList()
        .stream()
        .map(CompletableFuture::join)
        .toList();
  }
}
