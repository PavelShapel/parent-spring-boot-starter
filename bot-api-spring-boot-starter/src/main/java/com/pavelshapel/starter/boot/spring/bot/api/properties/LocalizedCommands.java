package com.pavelshapel.starter.boot.spring.bot.api.properties;

import java.util.Set;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record LocalizedCommands(
    @DefaultValue Set<String> languageCodes, @DefaultValue Set<CommandDescription> commands) {}
