package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.log.spring.boot.starter.LoggerProvider;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class Listener<C extends ClientService<?>>
    implements Consumer<ContextRegistry>, LoggerProvider {
  public static final String DOT_IS_APPLICABLE_SIGNATURE = ".isApplicable(#contextRegistry)";

  private final C clientService;
  private final ApplicationEventPublisher events;
  private final Logger logger;

  protected Listener(C clientService, ApplicationEventPublisher events, Logger logger) {
    this.clientService = clientService;
    this.events = events;
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

  public boolean isApplicable(ContextRegistry contextRegistry) {
    return true;
  }
}
