package com.pavelshapel.starter.boot.spring.bot.api.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.Context;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponentsProcessor;
import java.util.HashMap;
import java.util.List;

public abstract class ContextExtractorsProcessor<
        PAYLOAD, EXTRACTOR extends ContextExtractor<PAYLOAD>>
    extends OrderedComponentsProcessor<PAYLOAD, Context, EXTRACTOR> {
  protected ContextExtractorsProcessor(List<EXTRACTOR> contextExtractors) {
    super(contextExtractors);
  }

  public final ContextRegistry getContextRegistry(PAYLOAD payload) {
    ContextRegistry contextRegistry = new ContextRegistry(/* contexts= */ new HashMap<>());
    apply(payload).forEach(contextRegistry::add);
    return contextRegistry;
  }
}
