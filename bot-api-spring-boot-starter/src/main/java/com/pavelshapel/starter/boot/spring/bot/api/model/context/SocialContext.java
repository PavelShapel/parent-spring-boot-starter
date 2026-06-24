package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import com.pavelshapel.starter.boot.spring.bot.api.model.SocialType;

public record SocialContext(Long id, SocialType type, String replierSimpleName)
    implements Context {}
