package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractor;
import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractorsProcessor;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class PayloadConsumer<
        PAYLOAD,
        EXTRACTORS_PROCESSOR extends
            ContextExtractorsProcessor<PAYLOAD, ? extends ContextExtractor<PAYLOAD>>>
    implements LoggerProvider {
  private final TransactionTemplate transactionTemplate;
  private final ApplicationEventPublisher events;
  private final EXTRACTORS_PROCESSOR contextExtractorsProcessor;
  private final Logger logger;

  protected PayloadConsumer(
      TransactionTemplate transactionTemplate,
      ApplicationEventPublisher events,
      EXTRACTORS_PROCESSOR contextExtractorsProcessor,
      Logger logger) {
    this.transactionTemplate = transactionTemplate;
    this.events = events;
    this.contextExtractorsProcessor = contextExtractorsProcessor;
    this.logger = logger;
  }

  protected final void consumeRawPayload(PAYLOAD payload, String payloadId) {
    logger.info("Consuming [{}] with id: [{}]", payload.getClass().getSimpleName(), payloadId);
    transactionTemplate.executeWithoutResult(
        _ -> events.publishEvent(contextExtractorsProcessor.getContextRegistry(payload)));
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }
}
