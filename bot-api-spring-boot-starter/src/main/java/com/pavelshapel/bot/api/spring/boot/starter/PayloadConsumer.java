package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.log.spring.boot.starter.LoggerProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PayloadConsumer<P, C extends PayloadConverter<P>> implements LoggerProvider {
  @Autowired private WorkersProcessor workersProcessor;
  @Autowired private C payloadConverter;
  @Autowired private Logger logger;

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
