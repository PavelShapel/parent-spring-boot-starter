package com.pavelshapel.bot.api.spring.boot.starter.model.context;

import com.pavelshapel.bot.api.spring.boot.starter.model.SocialType;

public record SocialContext(Long id, SocialType socialType) implements Context {}
