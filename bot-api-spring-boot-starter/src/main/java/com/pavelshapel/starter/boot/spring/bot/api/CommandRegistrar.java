package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.properties.BotProperties;
import com.pavelshapel.starter.boot.spring.bot.api.properties.LocalizedCommands;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import org.slf4j.Logger;

public abstract class CommandRegistrar<R, C extends ClientService<R, ?, ?>>
    implements LoggerProvider {
  private final BotProperties botProperties;
  private final C clientService;
  private final BotMessageSourceService botMessageSourceService;
  private final Logger logger;

  protected CommandRegistrar(
      BotProperties botProperties,
      C clientService,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    this.botProperties = botProperties;
    this.clientService = clientService;
    this.botMessageSourceService = botMessageSourceService;
    this.logger = logger;
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }

  protected final String getMessage(String languageCode, String key) {
    return botMessageSourceService.get(languageCode, key);
  }

  protected final LocalizedCommands getLocalizedCommands() {
    return botProperties.localizedCommands();
  }

  protected final void execute(R request) {
    clientService.execute(
        "Send [%s] request".formatted(request.getClass().getSimpleName()), request);
  }

  public abstract void register();
}
