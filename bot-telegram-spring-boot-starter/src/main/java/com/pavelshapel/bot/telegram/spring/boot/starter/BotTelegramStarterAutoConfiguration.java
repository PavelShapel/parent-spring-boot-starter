package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.BotMessageSourceService;
import com.pavelshapel.bot.api.spring.boot.starter.properties.BotProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@AutoConfiguration
final class BotTelegramStarterAutoConfiguration {
  private static final Logger log =
      LoggerFactory.getLogger(BotTelegramStarterAutoConfiguration.class);

  BotTelegramStarterAutoConfiguration() {
    log.info("bot-telegram-spring-boot-starter was applied ✅");
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramBotContextExtractor telegramBotContextExtractor(BotProperties botProperties) {
    return new TelegramBotContextExtractor(botProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramClient telegramClient(BotProperties botProperties) {
    return new OkHttpTelegramClient(botProperties.token());
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramClientService telegramClientService(Logger logger, TelegramClient telegramClient) {
    return new TelegramClientService(logger, telegramClient);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramCommandRegistrar telegramCommandRegistrar(
      BotProperties botProperties,
      TelegramClientService clientService,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    return new TelegramCommandRegistrar(
        botProperties, clientService, botMessageSourceService, logger);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramContextExtractorsProcessor telegramContextExtractorsProcessor(
      List<TelegramContextExtractor> telegramWorkerNestedContextExtractors) {
    return new TelegramContextExtractorsProcessor(telegramWorkerNestedContextExtractors);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramPayloadConsumer telegramPayloadConsumer(
      TransactionTemplate transactionTemplate,
      ApplicationEventPublisher events,
      TelegramContextExtractorsProcessor contextExtractorsProcessor,
      Logger logger) {
    return new TelegramPayloadConsumer(
        transactionTemplate, events, contextExtractorsProcessor, logger);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramSocialContextExtractor telegramSocialContextExtractor() {
    return new TelegramSocialContextExtractor();
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramSpringBot telegramSpringBot(
      TelegramPayloadConsumer payloadConsumer, BotProperties botProperties) {
    return new TelegramSpringBot(payloadConsumer, botProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramTextChatWorkerContextExtractor telegramTextChatContextExtractor() {
    return new TelegramTextChatWorkerContextExtractor();
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramTextMessageWorkerContextExtractor telegramTextMessageWorkerContextExtractor() {
    return new TelegramTextMessageWorkerContextExtractor();
  }

  @Bean
  @ConditionalOnMissingBean
  TelegramTextUserWorkerContextExtractor telegramTextUserWorkerContextExtractor() {
    return new TelegramTextUserWorkerContextExtractor();
  }

  @Bean
  @ConditionalOnProperty(prefix = "bot.localized-commands.commands[0]", name = "command")
  ApplicationListener<ApplicationReadyEvent> telegramCommandsInitializer(
      TelegramCommandRegistrar telegramCommandRegistrar) {
    return _ -> telegramCommandRegistrar.register();
  }
}
