package com.pavelshapel.starter.boot.spring.bot.api.model.context;

public record UserContext(
    Long id,
    Long socialId,
    String firstName,
    String lastName,
    String nickName,
    String languageCode,
    Boolean isPremium)
    implements Context {}
