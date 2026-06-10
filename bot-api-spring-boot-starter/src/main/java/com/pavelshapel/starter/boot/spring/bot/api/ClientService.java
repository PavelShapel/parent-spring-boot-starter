package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ChatContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

public abstract class ClientService<R> implements LoggerProvider {
  private final Logger logger;

  protected ClientService(Logger logger) {
    this.logger = logger;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  public abstract void sendMessage(ContextRegistry contextRegistry);

  public abstract void editMessage(ContextRegistry contextRegistry);

  public abstract void deleteMessage(ContextRegistry contextRegistry);

  public final void execute(ContextRegistry contextRegistry, R request) {
    ChatContext chatContext = contextRegistry.get(ChatContext.class);
    MessageContext messageContext = contextRegistry.get(MessageContext.class);
    execute(
        "[%s] chatId [%d], messageId [%d], text [%s]"
            .formatted(
                request.getClass().getSimpleName(),
                chatContext.socialId(),
                messageContext.socialId(),
                messageContext.message()),
        request);
  }

  public void execute(String message, R request) {
    executeAndLog(
        Optional.ofNullable(message)
            .filter(StringUtils::hasText)
            .orElse("Send [%s] request".formatted(request.getClass().getSimpleName())),
        () -> execute(request));
  }

  protected abstract void execute(R request);
}
