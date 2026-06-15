package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ListenerContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.SocialContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.UserContext;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class Listener<C extends ClientService<?, ?, ?>>
    implements Consumer<ContextRegistry>, LoggerProvider {
  public static final String DOT_IS_APPLICABLE_SIGNATURE = ".isApplicable(#contextRegistry)";

  private final C clientService;
  private final ApplicationEventPublisher events;
  private final BotMessageSourceService botMessageSourceService;
  private final Logger logger;

  protected Listener(
      C clientService,
      ApplicationEventPublisher events,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    this.clientService = clientService;
    this.events = events;
    this.botMessageSourceService = botMessageSourceService;
    this.logger = logger;
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }

  protected final void sendMessage(ContextRegistry contextRegistry) {
    clientService.sendMessage(contextRegistry);
  }

  protected final void editMessage(ContextRegistry contextRegistry) {
    clientService.editMessage(contextRegistry);
  }

  protected final void deleteMessage(ContextRegistry contextRegistry) {
    clientService.deleteMessage(contextRegistry);
  }

  protected final void publishEvent(ContextRegistry contextRegistry) {
    events.publishEvent(contextRegistry);
  }

  protected final String getMessageFromSource(
      ContextRegistry contextRegistry, String key, Object... args) {
    return botMessageSourceService.get(contextRegistry, key, args);
  }

  protected final void acceptAndLog(ContextRegistry contextRegistry) {
    contextRegistry.add(new ListenerContext(getClass().getSimpleName()));
    executeAndLog(
        "[%s] received message [%s] from user with socialId [%d], socialType [%s]"
            .formatted(
                getClass().getSimpleName(),
                contextRegistry.get(MessageContext.class).message(),
                contextRegistry.get(UserContext.class).socialId(),
                contextRegistry.get(SocialContext.class).type()),
        () -> execute(contextRegistry));
  }

  public boolean isApplicable(ContextRegistry contextRegistry) {
    return isMessageContainsThisClassName(contextRegistry);
  }

  protected final boolean isMessageContainsThisClassName(ContextRegistry contextRegistry) {
    return contextRegistry.get(MessageContext.class).message().contains(getClass().getSimpleName());
  }

  protected abstract void execute(ContextRegistry contextRegistry);
}
