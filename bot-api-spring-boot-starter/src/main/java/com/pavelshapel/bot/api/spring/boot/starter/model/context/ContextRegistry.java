package com.pavelshapel.bot.api.spring.boot.starter.model.context;

import java.util.HashMap;
import java.util.Map;

public final class ContextRegistry {
  private final Map<String, Context> contexts = new HashMap<>();

  public void add(Context context) {
    contexts.put(getKey(context.getClass()), context);
  }

  @SuppressWarnings("unchecked")
  public <C extends Context> C get(Class<C> contextClass) {
    return (C) contexts.get(getKey(contextClass));
  }

  private static <C extends Context> String getKey(Class<C> contextClass) {
    return contextClass.getSimpleName();
  }
}
