package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.UserContext;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramTextUserContextExtractor extends TelegramTextContextExtractor {
  @Override
  public UserContext apply(Update update) {
    return toUserContext(update.getMessage().getFrom());
  }
}
