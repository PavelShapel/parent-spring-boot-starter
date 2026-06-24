package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;
import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractor;
import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractorsProcessor;

public abstract class SpringBot<
    PAYLOAD,
    PAYLOAD_CONSUMER extends
        PayloadConsumer<
                PAYLOAD,
                ? extends
                    ContextExtractorsProcessor<PAYLOAD, ? extends ContextExtractor<PAYLOAD>>>> {
  private final PAYLOAD_CONSUMER payloadConsumer;
  private final BotProperties botProperties;

  protected SpringBot(PAYLOAD_CONSUMER payloadConsumer, BotProperties botProperties) {
    this.payloadConsumer = payloadConsumer;
    this.botProperties = botProperties;
  }

  protected final String getName() {
    return botProperties.name();
  }

  protected final String getToken() {
    return botProperties.token();
  }

  protected final PAYLOAD_CONSUMER getPayloadConsumer() {
    return payloadConsumer;
  }
}
