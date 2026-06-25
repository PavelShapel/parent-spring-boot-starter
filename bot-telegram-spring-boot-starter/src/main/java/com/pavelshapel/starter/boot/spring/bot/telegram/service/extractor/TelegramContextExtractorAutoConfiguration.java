package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;
import com.pavelshapel.starter.boot.spring.bot.telegram.service.replier.TelegramRepliersProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
final class TelegramContextExtractorAutoConfiguration {
  @Bean
  TelegramBotContextExtractor telegramBotContextExtractor(BotProperties botProperties) {
    return new TelegramBotContextExtractor(botProperties);
  }

  @Bean
  TelegramCallbackChatContextExtractor telegramCallbackChatContextExtractor() {
    return new TelegramCallbackChatContextExtractor();
  }

  @Bean
  TelegramCallbackMessageContextExtractor telegramCallbackMessageContextExtractor() {
    return new TelegramCallbackMessageContextExtractor();
  }

  @Bean
  TelegramCallbackUserContextExtractor telegramCallbackUserContextExtractor() {
    return new TelegramCallbackUserContextExtractor();
  }

  @Bean
  TelegramTextChatContextExtractor telegramTextChatContextExtractor() {
    return new TelegramTextChatContextExtractor();
  }

  @Bean
  TelegramTextMessageContextExtractor telegramTextMessageContextExtractor() {
    return new TelegramTextMessageContextExtractor();
  }

  @Bean
  TelegramTextUserContextExtractor telegramTextUserContextExtractor() {
    return new TelegramTextUserContextExtractor();
  }

  @Bean
  TelegramSocialContextExtractor telegramSocialContextExtractor(
      TelegramRepliersProcessor telegramRepliersProcessor) {
    return new TelegramSocialContextExtractor(telegramRepliersProcessor);
  }
}
