package com.pavelshapel.starter.boot.spring.ordered;

import java.util.List;

public abstract class SequentialOrderedComponentsProcessor<
        PAYLOAD, RESULT, COMPONENT extends OrderedComponent<PAYLOAD, RESULT>>
    extends OrderedComponentsProcessor<PAYLOAD, RESULT, COMPONENT> {

  protected SequentialOrderedComponentsProcessor(List<COMPONENT> components) {
    super(components);
  }

  @Override
  protected final List<OrderedResult<RESULT>> apply(PAYLOAD payload) {
    return getApplicableComponentsStream(payload)
        .map(component -> component.apply(payload))
        .toList();
  }
}
