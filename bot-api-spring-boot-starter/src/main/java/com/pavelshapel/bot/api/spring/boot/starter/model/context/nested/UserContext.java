package com.pavelshapel.bot.api.spring.boot.starter.model.context.nested;

import java.util.Locale;

public record UserContext(
    Long id, Long socialId, String nickName, String email, Locale locale, Boolean isPremium) {}
