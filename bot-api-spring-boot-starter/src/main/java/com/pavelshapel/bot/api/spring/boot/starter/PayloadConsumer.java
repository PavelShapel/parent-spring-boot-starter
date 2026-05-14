package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.log.spring.boot.starter.LoggerProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PayloadConsumer<P, C extends PayloadConverter<P>> implements LoggerProvider {
  @Autowired private WorkersProcessor workersProcessor;

  private final C payloadConverter;
  private final Logger logger;

  protected PayloadConsumer(C payloadConverter, Logger logger) {
    this.payloadConverter = payloadConverter;
    this.logger = logger;
  }

  protected final void consumeRawPayload(P payload) {
    executeAndLog(
        "Proxying payload [%s]".formatted(payload.getClass().getSimpleName()),
        () -> workersProcessor.apply(payloadConverter.convert(payload)));
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }
}
