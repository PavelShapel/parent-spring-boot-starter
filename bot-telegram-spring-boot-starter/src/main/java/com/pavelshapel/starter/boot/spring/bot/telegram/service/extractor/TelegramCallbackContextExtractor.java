package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import org.telegram.telegrambots.meta.api.objects.Update;

abstract class TelegramCallbackContextExtractor extends TelegramContextExtractor {
  @Override
  protected final boolean isApplicable(Update update) {
    return update.hasCallbackQuery();
  }
}
