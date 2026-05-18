package com.pavelshapel.bot.api.spring.boot.starter.model.context.worker;

import com.pavelshapel.bot.api.spring.boot.starter.model.SocialType;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.Context;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested.BotContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested.ChatContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested.MessageContext;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested.UserContext;

public record WorkerContext(
    Long id,
    SocialType socialType,
    UserContext userContext,
    MessageContext messageContext,
    ChatContext chatContext,
    BotContext botContext)
    implements Context {}
