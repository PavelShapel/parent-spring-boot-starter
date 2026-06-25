package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import static java.util.stream.Collectors.toUnmodifiableSet;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

final class TelegramTextMessageContextExtractor extends TelegramTextContextExtractor {
  @Override
  public MessageContext apply(Update update) {
    Message message = update.getMessage();
    return new MessageContext(
        message.getMessageId().longValue(),
        message.getText(),
        message.getNewChatMembers().stream().map(this::toUserContext).collect(toUnmodifiableSet()));
  }
}
