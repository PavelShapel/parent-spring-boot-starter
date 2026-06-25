package com.pavelshapel.starter.boot.spring.bot.telegram.service.extractor;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ChatContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.UserContext;
import com.pavelshapel.starter.boot.spring.bot.api.service.extractor.ContextExtractor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

public abstract class TelegramContextExtractor extends ContextExtractor<Update> {
  private static final String UTC_ZONE_ID = "UTC";

  protected final UserContext toUserContext(User user) {
    return new UserContext(
        /* id= */ null,
        /* socialId= */ user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getUserName(),
        user.getLanguageCode(),
        user.getIsPremium());
  }

  protected final ChatContext toChatContext(Chat chat) {
    return new ChatContext(
        /* id= */ null, /* socialId= */ chat.getId(), chat.getType(), chat.getTitle(), UTC_ZONE_ID);
  }
}
