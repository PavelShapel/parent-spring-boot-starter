package com.pavelshapel.bot.api.spring.boot.starter.model.context;

import java.util.Set;

public record MessageContext(Long socialId, String message, Set<UserContext> newChatMembers)
    implements Context {}
