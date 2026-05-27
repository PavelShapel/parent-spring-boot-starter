package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.ClientService;
import com.pavelshapel.bot.api.spring.boot.starter.exception.ClientServiceApiException;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.ChatContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.MessageContext;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public final class TelegramClientService extends ClientService<BotApiMethod<?>> {
  private final TelegramClient telegramClient;

  TelegramClientService(Logger logger, TelegramClient telegramClient) {
    super(logger);
    this.telegramClient = telegramClient;
  }

  @Override
  public void sendMessage(ContextRegistry contextRegistry) {
    execute(
        contextRegistry,
        SendMessage.builder()
            .chatId(getChatId(contextRegistry))
            .text(getMessage(contextRegistry))
            // .replyMarkup(getInlineKeyboardMarkup(workerContext))
            .build());
  }

  @Override
  public void editMessage(ContextRegistry contextRegistry) {
    execute(
        contextRegistry,
        EditMessageText.builder()
            .chatId(getChatId(contextRegistry))
            .messageId(getMessageId(contextRegistry))
            .text(getMessage(contextRegistry))
            // .replyMarkup(getInlineKeyboardMarkup(workerContext))
            .build());
  }

  @Override
  public void deleteMessage(ContextRegistry contextRegistry) {
    execute(
        contextRegistry,
        DeleteMessage.builder()
            .chatId(getChatId(contextRegistry))
            .messageId(getMessageId(contextRegistry))
            .build());
  }

  @Override
  public void execute(BotApiMethod<?> request) {
    try {
      telegramClient.execute(request);
    } catch (TelegramApiException exception) {
      throw new ClientServiceApiException(
          "Telegram API exception on [%s]".formatted(request.getMethod()), exception);
    }
  }

  private static Long getChatId(ContextRegistry contextRegistry) {
    return contextRegistry.get(ChatContext.class).socialId();
  }

  private static int getMessageId(ContextRegistry contextRegistry) {
    return contextRegistry.get(MessageContext.class).socialId().intValue();
  }

  private static String getMessage(ContextRegistry contextRegistry) {
    return contextRegistry.get(MessageContext.class).message();
  }
}
