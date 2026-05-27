package com.pavelshapel.bot.telegram.spring.boot.starter;

import static com.pavelshapel.bot.api.spring.boot.starter.model.SocialType.TELEGRAM;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.SocialContext;
import org.telegram.telegrambots.meta.api.objects.Update;

final class TelegramSocialContextExtractor extends TelegramContextExtractor {
  @Override
  public SocialContext apply(Update update) {
    return new SocialContext(update.getUpdateId().longValue(), TELEGRAM);
  }
}
