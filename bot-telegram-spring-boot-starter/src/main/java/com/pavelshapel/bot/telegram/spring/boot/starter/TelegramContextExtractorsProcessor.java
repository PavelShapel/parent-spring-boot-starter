package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.ContextExtractorsProcessor;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramContextExtractorsProcessor
    extends ContextExtractorsProcessor<Update, TelegramContextExtractor> {
  TelegramContextExtractorsProcessor(
      List<TelegramContextExtractor> telegramWorkerNestedContextExtractors) {
    super(telegramWorkerNestedContextExtractors);
  }
}
