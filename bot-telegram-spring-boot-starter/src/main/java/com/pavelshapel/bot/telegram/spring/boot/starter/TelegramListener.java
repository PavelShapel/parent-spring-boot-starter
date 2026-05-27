package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.Listener;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.MessageContext;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class TelegramListener extends Listener<TelegramClientService> {

  protected TelegramListener(
      TelegramClientService clientService, ApplicationEventPublisher events, Logger logger) {
    super(clientService, events, logger);
  }

  protected boolean isMessageStartsWithThisClassName(ContextRegistry contextRegistry) {
    return contextRegistry
        .get(MessageContext.class)
        .message()
        .startsWith(getClass().getSimpleName());
  }
}
