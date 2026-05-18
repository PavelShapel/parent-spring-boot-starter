package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.ordered.spring.boot.starter.OrderedComponentsProcessor;
import java.util.List;

final class WorkersProcessor extends OrderedComponentsProcessor<ContextRegistry, Void, Worker> {
  WorkersProcessor(List<Worker> workers) {
    super(workers);
  }
}
