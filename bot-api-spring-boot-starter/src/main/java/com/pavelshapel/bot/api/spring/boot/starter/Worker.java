package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.WorkerContext;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponent;

public abstract class Worker extends OrderedComponent<WorkerContext, Void> {
  @Override
  public final Void apply(WorkerContext workerContext) {
    doWork(workerContext);
    return null;
  }

  protected abstract void doWork(WorkerContext workerContext);
}
