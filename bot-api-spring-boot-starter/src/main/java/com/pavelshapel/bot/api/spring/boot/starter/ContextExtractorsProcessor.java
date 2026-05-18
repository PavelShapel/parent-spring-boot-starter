package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.Context;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponentsProcessor;
import java.util.List;

public abstract class ContextExtractorsProcessor<P, C extends ContextExtractor<P>>
    extends OrderedComponentsProcessor<P, Context, C> {

  protected ContextExtractorsProcessor(List<C> contextExtractors) {
    super(contextExtractors);
  }
}
