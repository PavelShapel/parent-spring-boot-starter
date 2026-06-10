package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;

public abstract class SpringBot<
    P,
    C extends
        PayloadConsumer<
                P, ? extends ContextExtractorsProcessor<P, ? extends ContextExtractor<P>>>> {
  private final C payloadConsumer;
  private final BotProperties botProperties;

  protected SpringBot(C payloadConsumer, BotProperties botProperties) {
    this.payloadConsumer = payloadConsumer;
    this.botProperties = botProperties;
  }

  protected final String getName() {
    return botProperties.name();
  }

  protected final String getToken() {
    return botProperties.token();
  }

  protected final C getPayloadConsumer() {
    return payloadConsumer;
  }
}
