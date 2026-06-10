package com.pavelshapel.starter.boot.spring.bot.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "bot")
public record BotProperties(
    @DefaultValue("${BOT_NAME}") String name,
    @DefaultValue("${BOT_TOKEN}") String token,
    Client client,
    LocalizedCommands localizedCommands) {}
