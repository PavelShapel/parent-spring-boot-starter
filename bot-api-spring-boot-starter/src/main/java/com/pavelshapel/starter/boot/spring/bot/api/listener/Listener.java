package com.pavelshapel.starter.boot.spring.bot.api.listener;

import com.pavelshapel.starter.boot.spring.bot.api.BotMessageSourceService;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ListenerContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.SocialContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.UserContext;
import com.pavelshapel.starter.boot.spring.bot.api.replier.Replier;
import com.pavelshapel.starter.boot.spring.bot.api.replier.RepliersProcessor;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

public abstract class Listener<REPLIERS_PROCESSOR extends RepliersProcessor<?, ?>>
    implements Consumer<ContextRegistry>, LoggerProvider {
  public static final String DOT_IS_APPLICABLE_SIGNATURE = ".isApplicable(#contextRegistry)";

  private final REPLIERS_PROCESSOR repliersProcessor;
  private final ApplicationEventPublisher events;
  private final BotMessageSourceService botMessageSourceService;
  private final Logger logger;

  protected Listener(
      REPLIERS_PROCESSOR repliersProcessor,
      ApplicationEventPublisher events,
      BotMessageSourceService botMessageSourceService,
      Logger logger) {
    this.repliersProcessor = repliersProcessor;
    this.events = events;
    this.botMessageSourceService = botMessageSourceService;
    this.logger = logger;
  }

  @Override
  public final Logger getLogger() {
    return logger;
  }

  protected final void replyToMessage(ContextRegistry contextRegistry) {
    getReplierByName(contextRegistry).reply(contextRegistry);
  }

  protected final void deleteMessage(ContextRegistry contextRegistry) {
    getReplierByName(contextRegistry).delete(contextRegistry);
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
                contextRegistry.getMessageText(),
                contextRegistry.get(UserContext.class).socialId(),
                contextRegistry.get(SocialContext.class).type()),
        () -> execute(contextRegistry));
  }

  public boolean isApplicable(ContextRegistry contextRegistry) {
    return isMessageContainsThisClassName(contextRegistry);
  }

  protected final boolean isMessageContainsThisClassName(ContextRegistry contextRegistry) {
    return contextRegistry.getMessageText().contains(getClass().getSimpleName());
  }

  protected abstract void execute(ContextRegistry contextRegistry);

  private Replier<?, ?> getReplierByName(ContextRegistry contextRegistry) {
    return repliersProcessor.getReplierByName(
        contextRegistry.get(SocialContext.class).replierSimpleName());
  }
}
