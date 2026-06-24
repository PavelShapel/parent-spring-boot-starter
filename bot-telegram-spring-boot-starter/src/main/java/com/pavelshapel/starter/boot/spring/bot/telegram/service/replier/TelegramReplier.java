package com.pavelshapel.starter.boot.spring.bot.telegram.service.replier;

import com.pavelshapel.starter.boot.spring.bot.api.service.replier.Replier;
import com.pavelshapel.starter.boot.spring.bot.telegram.TelegramClientService;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class TelegramReplier extends Replier<Update, TelegramClientService> {
  protected TelegramReplier(TelegramClientService telegramClientService) {
    super(telegramClientService);
  }
}
