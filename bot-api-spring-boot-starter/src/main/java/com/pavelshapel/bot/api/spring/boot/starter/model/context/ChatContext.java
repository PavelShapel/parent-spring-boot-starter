package com.pavelshapel.bot.api.spring.boot.starter.model.context;

public record ChatContext(Long id, Long socialId, String type, String title) implements Context {}
