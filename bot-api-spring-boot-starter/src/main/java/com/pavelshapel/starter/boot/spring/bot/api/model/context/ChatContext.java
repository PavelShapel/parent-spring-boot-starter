package com.pavelshapel.starter.boot.spring.bot.api.model.context;

public record ChatContext(Long id, Long socialId, String type, String title, String timezoneId)
    implements Context {}
