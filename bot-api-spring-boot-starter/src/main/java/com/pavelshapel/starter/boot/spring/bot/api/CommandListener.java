package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class CommandListener<C extends ClientService<?, ?, ?>> extends Listener<C> {
  protected CommandListener(
      C clientService,
      ApplicationEventPublisher events,
      BotMessageSourceService messageSourceService,
      Logger logger) {
    super(clientService, events, messageSourceService, logger);
  }

  @Override
  public boolean isApplicable(ContextRegistry contextRegistry) {
    return getCommand().equals(contextRegistry.get(MessageContext.class).message());
  }

  protected abstract String getCommand();
}
