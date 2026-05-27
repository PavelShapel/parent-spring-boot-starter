package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.ContextExtractor;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.ChatContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.UserContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

abstract class TelegramContextExtractor extends ContextExtractor<Update> {
  protected final UserContext createUserContext(User user) {
    return new UserContext(
        /* id= */ null,
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getUserName(),
        /* email= */ null,
        user.getLanguageCode(),
        user.getIsPremium());
  }

  protected final ChatContext createChatContext(Chat chat) {
    return new ChatContext(/* id= */ null, chat.getId(), chat.getType(), chat.getTitle());
  }
}
