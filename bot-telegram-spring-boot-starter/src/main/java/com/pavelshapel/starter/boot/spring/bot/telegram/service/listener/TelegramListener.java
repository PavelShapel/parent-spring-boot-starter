package com.pavelshapel.starter.boot.spring.bot.telegram.service.listener;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.service.listener.Listener;
import com.pavelshapel.starter.boot.spring.bot.telegram.service.replier.TelegramRepliersProcessor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class TelegramListener extends Listener<TelegramRepliersProcessor> {
  protected TelegramListener(
      TelegramRepliersProcessor repliersProcessor,
      ApplicationEventPublisher events,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    super(repliersProcessor, events, botMessageSourceService, logger);
  }
}
