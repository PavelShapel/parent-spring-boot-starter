package com.pavelshapel.bot.api.spring.boot.starter;

public abstract class SpringBot<P, C extends PayloadConverter<P>> {
  private final PayloadConsumer<P, C> payloadConsumer;
  private final BotProperties botProperties;

  protected SpringBot(PayloadConsumer<P, C> payloadConsumer, BotProperties botProperties) {
    this.payloadConsumer = payloadConsumer;
    this.botProperties = botProperties;
  }

  protected final String getName() {
    return botProperties.name();
  }

  protected final String getToken() {
    return botProperties.token();
  }

  protected final PayloadConsumer<P, C> getPayloadConsumer() {
    return payloadConsumer;
  }
}
