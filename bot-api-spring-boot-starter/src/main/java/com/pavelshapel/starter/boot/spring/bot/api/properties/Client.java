package com.pavelshapel.starter.boot.spring.bot.api.properties;

import java.time.Duration;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record Client(
    @DefaultValue("5s") Duration connectTimeout,
    @DefaultValue("5s") Duration writeTimeout,
    @DefaultValue("5s") Duration readTimeout,
    @DefaultValue("true") boolean retryOnConnectionFailure) {}
