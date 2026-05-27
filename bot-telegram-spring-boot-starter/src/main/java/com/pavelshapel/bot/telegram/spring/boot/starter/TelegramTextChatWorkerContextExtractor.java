package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ChatContext;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramTextChatWorkerContextExtractor extends TelegramTextContextExtractor {
  @Override
  public ChatContext apply(Update update) {
    return createChatContext(update.getMessage().getChat());
  }
}
