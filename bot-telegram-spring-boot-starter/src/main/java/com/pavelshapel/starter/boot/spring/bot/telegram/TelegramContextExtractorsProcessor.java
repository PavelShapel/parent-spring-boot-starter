package com.pavelshapel.starter.boot.spring.bot.telegram;

import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractorsProcessor;
import com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor.TelegramContextExtractor;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramContextExtractorsProcessor
    extends ContextExtractorsProcessor<Update, TelegramContextExtractor> {
  TelegramContextExtractorsProcessor(
      List<TelegramContextExtractor> telegramWorkerNestedContextExtractors) {
    super(telegramWorkerNestedContextExtractors);
  }
}
