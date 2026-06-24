package com.pavelshapel.starter.boot.spring.bot.telegram.service.replier;

import static java.util.Objects.isNull;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.telegram.TelegramClientService;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramCallbackReplier extends TelegramReplier {

  TelegramCallbackReplier(TelegramClientService telegramClientService) {
    super(telegramClientService);
  }

  @Override
  public void reply(ContextRegistry contextRegistry) {
    getClientService().editMessage(contextRegistry);
  }

  @Override
  protected boolean isApplicable(Update update) {
    return isNull(update) || update.hasCallbackQuery();
  }
}
