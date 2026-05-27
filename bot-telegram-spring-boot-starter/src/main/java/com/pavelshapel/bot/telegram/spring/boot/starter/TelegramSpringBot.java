package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.SpringBot;
import com.pavelshapel.bot.api.spring.boot.starter.properties.BotProperties;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramSpringBot extends SpringBot<Update, TelegramPayloadConsumer>
    implements SpringLongPollingBot {
  TelegramSpringBot(TelegramPayloadConsumer payloadConsumer, BotProperties botProperties) {
    super(payloadConsumer, botProperties);
  }

  @Override
  public String getBotToken() {
    return getToken();
  }

  @Override
  public LongPollingUpdateConsumer getUpdatesConsumer() {
    return getPayloadConsumer();
  }
}
