package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import java.util.Set;

public record MessageContext(Long socialId, String message, Set<UserContext> newChatMembers)
    implements Context {

  public MessageContext withMessage(String message) {
    return new MessageContext(this.socialId, message, this.newChatMembers);
  }
}
