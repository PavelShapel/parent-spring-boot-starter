package com.pavelshapel.starter.boot.spring.bot.telegram.service.replier;

import com.pavelshapel.starter.boot.spring.bot.api.service.replier.RepliersProcessor;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

public final class TelegramRepliersProcessor extends RepliersProcessor<Update, TelegramReplier> {
  TelegramRepliersProcessor(List<TelegramReplier> telegramMessageTypeResolvers) {
    super(telegramMessageTypeResolvers);
  }
}
