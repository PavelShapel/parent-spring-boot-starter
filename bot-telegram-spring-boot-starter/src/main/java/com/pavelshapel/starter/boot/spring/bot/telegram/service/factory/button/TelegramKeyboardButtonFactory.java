package com.pavelshapel.starter.boot.spring.bot.telegram.service.factory.button;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.service.factory.button.KeyboardButtonFactory;

public abstract class TelegramKeyboardButtonFactory
    extends KeyboardButtonFactory<TelegramKeyboardButton> {
  protected TelegramKeyboardButtonFactory(BotMessageSourceService botMessageSourceService) {
    super(botMessageSourceService);
  }
}
