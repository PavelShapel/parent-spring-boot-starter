package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponent;

public abstract class Worker extends OrderedComponent<ContextRegistry, Void> {
  @Override
  public final Void apply(ContextRegistry contextRegistry) {
    doWork(contextRegistry);
    return null;
  }

  protected abstract void doWork(ContextRegistry contextRegistry);
}
