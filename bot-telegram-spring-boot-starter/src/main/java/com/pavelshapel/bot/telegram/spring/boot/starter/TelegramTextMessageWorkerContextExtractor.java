package com.pavelshapel.bot.telegram.spring.boot.starter;

import static java.util.stream.Collectors.toUnmodifiableSet;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.MessageContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

final class TelegramTextMessageWorkerContextExtractor extends TelegramTextContextExtractor {
  @Override
  public MessageContext apply(Update update) {
    Message message = update.getMessage();
    return new MessageContext(
        message.getMessageId().longValue(),
        message.getText(),
        message.getNewChatMembers().stream()
            .map(this::createUserContext)
            .collect(toUnmodifiableSet()));
  }
}
