package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.log.spring.boot.starter.LoggerProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PayloadConsumer<
        P, E extends ContextExtractorsProcessor<P, ? extends ContextExtractor<P>>>
    implements LoggerProvider {
  @Autowired private WorkersProcessor workersProcessor;

  private final E contextExtractorsProcessor;
  private final Logger logger;

  protected PayloadConsumer(E contextExtractorsProcessor, Logger logger) {
    this.contextExtractorsProcessor = contextExtractorsProcessor;
    this.logger = logger;
  }

  protected final void consumeRawPayload(P payload) {
    executeAndLog(
        "Proxying payload [%s]".formatted(payload.getClass().getSimpleName()),
        () -> workersProcessor.apply(contextExtractorsProcessor.getContextRegistry(payload)));
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }
}
