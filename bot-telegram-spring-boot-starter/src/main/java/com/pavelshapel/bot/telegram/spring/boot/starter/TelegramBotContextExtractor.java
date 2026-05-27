package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.BotContext;
import com.pavelshapel.bot.api.spring.boot.starter.properties.BotProperties;
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
