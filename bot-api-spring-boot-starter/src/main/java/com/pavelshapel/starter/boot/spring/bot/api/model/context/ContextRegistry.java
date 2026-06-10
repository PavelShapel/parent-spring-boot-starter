package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import java.util.HashMap;

public record ContextRegistry(HashMap<String, Context> contexts) {
  public ContextRegistry add(Context context) {
    contexts.put(getKey(context.getClass()), context);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <C extends Context> C get(Class<C> contextClass) {
    return (C) contexts.get(getKey(contextClass));
  }

  private static <C extends Context> String getKey(Class<C> contextClass) {
    return contextClass.getSimpleName();
  }
}
