package com.pavelshapel.bot.api.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class SpringBot<P, C extends PayloadConverter<P>> {
  @Autowired private PayloadConsumer<P, C> payloadConsumer;
  @Autowired private BotProperties botProperties;

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
