package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.UserContext;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramTextUserWorkerContextExtractor extends TelegramTextContextExtractor {
  @Override
  public UserContext apply(Update update) {
    return createUserContext(update.getMessage().getFrom());
  }
}
