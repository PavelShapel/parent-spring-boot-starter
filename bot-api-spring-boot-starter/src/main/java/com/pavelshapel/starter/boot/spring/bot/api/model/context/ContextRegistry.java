package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import java.util.HashMap;
import java.util.Set;
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

  public String getMessageText() {
    return get(MessageContext.class).message();
  }

  public ContextRegistry updateMessageText(String text) {
    return add(get(MessageContext.class).withMessage(text));
  }

  public <BUTTON extends KeyboardButton> ContextRegistry addButton(BUTTON keyboardButton) {
    return add(get(KeyboardButtonContext.class).withButton(keyboardButton));
  }

  public <BUTTON extends KeyboardButton> ContextRegistry addButtons(Set<BUTTON> keyboardButtons) {
    KeyboardButtonContext keyboardButtonContext = get(KeyboardButtonContext.class);
    keyboardButtons.forEach(keyboardButtonContext::withButton);
    return add(keyboardButtonContext);
  }

  public ContextRegistry apply(Consumer<ContextRegistry> action) {
    action.accept(this);
    return this;
  }

  private static <C extends Context> String getKey(Class<C> contextClass) {
    return contextClass.getSimpleName();
  }
}
