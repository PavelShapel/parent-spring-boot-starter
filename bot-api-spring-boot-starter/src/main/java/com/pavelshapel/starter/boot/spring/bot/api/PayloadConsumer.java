package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class PayloadConsumer<
        P, E extends ContextExtractorsProcessor<P, ? extends ContextExtractor<P>>>
    implements LoggerProvider {
  private final TransactionTemplate transactionTemplate;
  private final ApplicationEventPublisher events;
  private final E contextExtractorsProcessor;
  private final Logger logger;

  protected PayloadConsumer(
      TransactionTemplate transactionTemplate,
      ApplicationEventPublisher events,
      E contextExtractorsProcessor,
      Logger logger) {
    this.transactionTemplate = transactionTemplate;
    this.events = events;
    this.contextExtractorsProcessor = contextExtractorsProcessor;
    this.logger = logger;
  }

  protected final void consumeRawPayload(P payload, String payloadId) {
    logger.info("Consuming payload with id: [{}]", payloadId);
    transactionTemplate.executeWithoutResult(
        _ -> events.publishEvent(contextExtractorsProcessor.getContextRegistry(payload)));
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }
}
