package com.pavelshapel.starter.boot.spring.bot.api.listener;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.replier.RepliersProcessor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class CommandListener<REPLIERS_PROCESSOR extends RepliersProcessor<?, ?>>
    extends Listener<REPLIERS_PROCESSOR> {
  protected CommandListener(
      REPLIERS_PROCESSOR repliersProcessor,
      ApplicationEventPublisher events,
      BotMessageSourceService messageSourceService,
      Logger logger) {
    super(repliersProcessor, events, messageSourceService, logger);
  }

  @Override
  public boolean isApplicable(ContextRegistry contextRegistry) {
    return getCommand().equals(contextRegistry.getMessageText());
  }

  protected abstract String getCommand();
}
