package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ChatContext;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramCallbackChatContextExtractor extends TelegramCallbackContextExtractor {
  @Override
  public ChatContext apply(Update update) {
    return toChatContext(update.getCallbackQuery().getMessage().getChat());
  }
}
