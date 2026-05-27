package com.pavelshapel.bot.telegram.spring.boot.starter;

import org.telegram.telegrambots.meta.api.objects.Update;

abstract class TelegramTextContextExtractor extends TelegramContextExtractor {
  @Override
  protected final boolean isApplicable(Update update) {
    return update.hasMessage();
  }
}
