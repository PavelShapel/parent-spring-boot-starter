package com.pavelshapel.starter.boot.spring.bot.telegram.service.listener;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.service.listener.CommandListener;
import com.pavelshapel.starter.boot.spring.bot.telegram.service.replier.TelegramRepliersProcessor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class TelegramCommandListener extends CommandListener<TelegramRepliersProcessor> {
  protected TelegramCommandListener(
      TelegramRepliersProcessor repliersProcessor,
      ApplicationEventPublisher events,
      BotMessageSourceService messageSourceService,
      Logger logger) {
    super(repliersProcessor, events, messageSourceService, logger);
  }
}
