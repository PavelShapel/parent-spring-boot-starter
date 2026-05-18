package com.pavelshapel.bot.api.spring.boot.starter.model.context;

import java.util.HashMap;
import java.util.Map;

public final class ContextRegistry {
  private final Map<String, Context> contexts = new HashMap<>();

  public void add(Context context) {
    contexts.put(getKey(context), context);
  }

  @SuppressWarnings("unchecked")
  public <C extends Context> C get(String key) {
    return (C) contexts.get(key);
  }

  private static String getKey(Context context) {
    return context.getClass().getSimpleName();
  }
}
