package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import static java.util.stream.Collectors.toUnmodifiableSet;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.MessageContext;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

final class TelegramCallbackMessageContextExtractor extends TelegramCallbackContextExtractor {
  @Override
  public MessageContext apply(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    Message message = (Message) callbackQuery.getMessage();
    return new MessageContext(
        message.getMessageId().longValue(),
        callbackQuery.getData(),
        message.getNewChatMembers().stream().map(this::toUserContext).collect(toUnmodifiableSet()));
  }
}
