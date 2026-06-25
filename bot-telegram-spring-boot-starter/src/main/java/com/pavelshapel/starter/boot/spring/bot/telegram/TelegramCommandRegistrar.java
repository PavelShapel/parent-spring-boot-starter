package com.pavelshapel.starter.boot.spring.bot.telegram;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.CommandRegistrar;
import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;
import com.pavelshapel.starter.boot.spring.bot.api.properties.CommandDescription;
import com.pavelshapel.starter.boot.spring.bot.api.properties.LocalizedCommands;
import java.util.Set;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

final class TelegramCommandRegistrar
    extends CommandRegistrar<BotApiMethod<?>, TelegramClientService> {
  TelegramCommandRegistrar(
      BotProperties botProperties,
      TelegramClientService telegramClientService,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    super(botProperties, telegramClientService, botMessageSourceService, logger);
  }

  @Override
  public void register() {
    LocalizedCommands localizedCommands = getLocalizedCommands();
    Set<String> languageCodes = localizedCommands.languageCodes();
    languageCodes.add(/*default*/ null);
    languageCodes.stream()
        .map(languageCode -> toSetMyCommands(languageCode, localizedCommands.commands()))
        .forEach(this::execute);
  }

  private SetMyCommands toSetMyCommands(
      String languageCode, Set<CommandDescription> commandDescriptions) {
    return SetMyCommands.builder()
        .languageCode(languageCode)
        .commands(
            commandDescriptions.stream()
                .map(commandDescription -> toBotCommand(languageCode, commandDescription))
                .toList())
        .build();
  }

  private BotCommand toBotCommand(String languageCode, CommandDescription commandDescription) {
    return BotCommand.builder()
        .command(commandDescription.command())
        .description(getMessageFromSource(languageCode, commandDescription.descriptionKey()))
        .build();
  }
}
