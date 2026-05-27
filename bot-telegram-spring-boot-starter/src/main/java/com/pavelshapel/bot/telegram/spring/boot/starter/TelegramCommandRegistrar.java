package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.BotMessageSourceService;
import com.pavelshapel.bot.api.spring.boot.starter.CommandRegistrar;
import com.pavelshapel.bot.api.spring.boot.starter.properties.BotProperties;
import com.pavelshapel.bot.api.spring.boot.starter.properties.CommandDescription;
import com.pavelshapel.bot.api.spring.boot.starter.properties.LocalizedCommands;
import java.util.Set;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

final class TelegramCommandRegistrar
    extends CommandRegistrar<BotApiMethod<?>, TelegramClientService> {
  TelegramCommandRegistrar(
      BotProperties botProperties,
      TelegramClientService clientService,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    super(botProperties, clientService, botMessageSourceService, logger);
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
        .description(getMessage(languageCode, commandDescription.descriptionKey()))
        .build();
  }
}
