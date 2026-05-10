package com.pavelshapel.bot.api.spring.boot.starter;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class SpringBot<P, C extends PayloadConverter<P>> {
  @Autowired private PayloadConsumer<P, C> payloadConsumer;
  @Autowired private BotProperties telegramBotProperties;

  protected final String getName() {
    return telegramBotProperties.name();
  }

  protected final String getToken() {
    return telegramBotProperties.token();
  }

  protected final PayloadConsumer<P, C> getPayloadConsumer() {
    return payloadConsumer;
  }
}
