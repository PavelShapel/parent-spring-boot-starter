package com.pavelshapel.starter.boot.spring.bot.telegram.service.replier;

import com.pavelshapel.starter.boot.spring.bot.telegram.TelegramClientService;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
final class TelegramMessageRendererAutoConfiguration {
  @Bean
  TelegramTextReplier telegramTextReplier(TelegramClientService telegramClientService) {
    return new TelegramTextReplier(telegramClientService);
  }

  @Bean
  TelegramCallbackReplier telegramCallbackReplier(TelegramClientService telegramClientService) {
    return new TelegramCallbackReplier(telegramClientService);
  }

  @Bean
  TelegramRepliersProcessor repliersProcessor(List<TelegramReplier> telegramRepliers) {
    return new TelegramRepliersProcessor(telegramRepliers);
  }
}
