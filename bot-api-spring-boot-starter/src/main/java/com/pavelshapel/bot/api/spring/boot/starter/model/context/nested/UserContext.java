package com.pavelshapel.bot.api.spring.boot.starter.model.context.nested;

public record UserContext(
    Long id,
    Long socialId,
    String nickName,
    String email,
    String languageCode,
    Boolean isPremium) {}
