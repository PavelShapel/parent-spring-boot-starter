package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.BotContext;
import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramBotContextExtractor extends TelegramContextExtractor {
  private final BotProperties botProperties;

  TelegramBotContextExtractor(BotProperties botProperties) {
    this.botProperties = botProperties;
  }

  @Override
  public BotContext apply(Update update) {
    return new BotContext(botProperties.name());
  }
}
