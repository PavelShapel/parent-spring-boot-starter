package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import java.util.HashMap;
import java.util.function.Consumer;

public record ContextRegistry(HashMap<String, Context> contexts) {
  public ContextRegistry add(Context context) {
    contexts.put(getKey(context.getClass()), context);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <CONTEXT extends Context> CONTEXT get(Class<CONTEXT> contextClass) {
    return (CONTEXT) contexts.get(getKey(contextClass));
  }

  public ContextRegistry updateMessageText(String text) {
    return add(get(MessageContext.class).withMessage(text));
  }

  public <BUTTON extends KeyboardButton> ContextRegistry addButton(BUTTON keyboardButton) {
    return add(get(KeyboardButtonContext.class).withButton(keyboardButton));
  }

  public ContextRegistry apply(Consumer<ContextRegistry> action) {
    action.accept(this);
    return this;
  }

  private static <C extends Context> String getKey(Class<C> contextClass) {
    return contextClass.getSimpleName();
  }
}
