package com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested;

import java.util.Set;

public record MessageContext(
    Long socialId, Set<CallBackData> callBackData, Set<UserContext> newChatMembers)
    implements NestedWorkerContext {}
