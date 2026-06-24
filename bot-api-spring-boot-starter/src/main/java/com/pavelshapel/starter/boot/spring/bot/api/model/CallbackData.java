package com.pavelshapel.starter.boot.spring.bot.api.model;

import static java.util.Objects.requireNonNull;

import org.jspecify.annotations.NonNull;

public record CallbackData(String listenerSimpleName, String entityId, String value) {
  private static final String SEPARATOR = ":";

  public CallbackData {
    requireNonNull(listenerSimpleName, /* message= */ "ListenerSimpleName cannot be null");
  }

  public static CallbackData fromString(String rawData) {
    requireNonNull(rawData, "Raw data string cannot be null");
    String[] parts = rawData.split(SEPARATOR, /* limit= */ 3);
    String listenerSimpleName = parts[0];
    String entityId = (parts.length > 1 && !parts[1].isEmpty()) ? parts[1] : null;
    String value = (parts.length > 2) ? parts[2] : null;
    return new CallbackData(listenerSimpleName, entityId, value);
  }

  @Override
  @NonNull
  public String toString() {
    if (entityId == null) {
      return listenerSimpleName;
    }
    if (value == null || value.isEmpty()) {
      return "%s%s%s".formatted(listenerSimpleName, SEPARATOR, entityId);
    }
    return "%s%s%s%s%s".formatted(listenerSimpleName, SEPARATOR, entityId, SEPARATOR, value);
  }
}
