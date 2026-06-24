package com.pavelshapel.starter.boot.spring.bot.api.service;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ChatContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import com.pavelshapel.starter.boot.spring.bot.api.service.factory.KeyboardFactory;
import com.pavelshapel.starter.boot.spring.log.LoggerProvider;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

public abstract class ClientService<
        REQUEST, KEYBOARD, KEYBOARD_FACTORY extends KeyboardFactory<?, KEYBOARD>>
    implements LoggerProvider {
  private final KEYBOARD_FACTORY keyboardFactory;
  private final Logger logger;

  protected ClientService(KEYBOARD_FACTORY keyboardFactory, Logger logger) {
    this.keyboardFactory = keyboardFactory;
    this.logger = logger;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  public abstract void sendMessage(ContextRegistry contextRegistry);

  public abstract void editMessage(ContextRegistry contextRegistry);

  public abstract void deleteMessage(ContextRegistry contextRegistry);

  public final void execute(ContextRegistry contextRegistry, REQUEST request) {
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

  public final void execute(String message, REQUEST request) {
    executeAndLog(
        Optional.ofNullable(message)
            .filter(StringUtils::hasText)
            .orElse("Send [%s] request".formatted(request.getClass().getSimpleName())),
        () -> execute(request));
  }

  protected final KEYBOARD buildKeyboard(ContextRegistry contextRegistry) {
    return keyboardFactory.apply(contextRegistry);
  }

  protected abstract void execute(REQUEST request);
}
