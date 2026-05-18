package com.pavelshapel.bot.api.spring.boot.starter.model.context.worker.nested;

public record UserContext(
    Long id,
    Long socialId,
    String firstName,
    String lastName,
    String nickName,
    String email,
    String languageCode,
    Boolean isPremium)
    implements NestedWorkerContext {}
