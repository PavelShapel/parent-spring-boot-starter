package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import static com.pavelshapel.starter.boot.spring.bot.api.model.SocialType.TELEGRAM;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.SocialContext;
import com.pavelshapel.starter.boot.spring.bot.telegram.service.replier.TelegramRepliersProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramSocialContextExtractor extends TelegramContextExtractor {
  private final TelegramRepliersProcessor telegramRepliersProcessor;

  TelegramSocialContextExtractor(TelegramRepliersProcessor telegramRepliersProcessor) {
    this.telegramRepliersProcessor = telegramRepliersProcessor;
  }

  @Override
  public SocialContext apply(Update update) {
    long updateId = update.getUpdateId().longValue();
    String applicableReplierSimpleName =
        telegramRepliersProcessor.getApplicableReplierSimpleName(update);
    return new SocialContext(updateId, TELEGRAM, applicableReplierSimpleName);
  }
}
