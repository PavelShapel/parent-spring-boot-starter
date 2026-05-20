package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.Context;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponentsProcessor;
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
