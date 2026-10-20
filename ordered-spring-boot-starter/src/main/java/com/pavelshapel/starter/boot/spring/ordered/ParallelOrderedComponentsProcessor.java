package com.pavelshapel.starter.boot.spring.ordered;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public abstract class ParallelOrderedComponentsProcessor<
        PAYLOAD, RESULT, COMPONENT extends OrderedComponent<PAYLOAD, RESULT>>
    extends OrderedComponentsProcessor<PAYLOAD, RESULT, COMPONENT> {

  protected ParallelOrderedComponentsProcessor(List<COMPONENT> components) {
    super(components);
  }

  @Override
  protected final List<OrderedResult<RESULT>> apply(PAYLOAD payload) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      return getApplicableComponentsStream(payload)
          .map(component -> CompletableFuture.supplyAsync(() -> component.apply(payload), executor))
          .toList()
          .stream()
          .map(CompletableFuture::join)
          .toList();
    }
  }
}
