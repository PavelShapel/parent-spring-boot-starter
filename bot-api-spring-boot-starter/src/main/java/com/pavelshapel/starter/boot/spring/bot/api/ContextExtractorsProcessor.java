package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.Context;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponentsProcessor;
import java.util.HashMap;
import java.util.List;

public abstract class ContextExtractorsProcessor<P, E extends ContextExtractor<P>>
    extends OrderedComponentsProcessor<P, Context, E> {
  protected ContextExtractorsProcessor(List<E> contextExtractors) {
    super(contextExtractors);
  }

  protected final ContextRegistry getContextRegistry(P payload) {
    ContextRegistry contextRegistry = new ContextRegistry(/* contexts= */ new HashMap<>());
    apply(payload).forEach(contextRegistry::add);
    return contextRegistry;
  }
}
