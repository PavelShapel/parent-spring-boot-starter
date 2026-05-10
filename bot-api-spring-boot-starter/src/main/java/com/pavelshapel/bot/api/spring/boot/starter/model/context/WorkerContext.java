package com.pavelshapel.bot.api.spring.boot.starter.model.context;

import com.pavelshapel.bot.api.spring.boot.starter.model.SocialType;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.nested.BotContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.nested.ChatContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.nested.MessageContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.nested.UserContext;

public record WorkerContext(
    Long id,
    SocialType socialType,
    UserContext userContext,
    MessageContext messageContext,
    ChatContext chatContext,
    BotContext botContext) {}
