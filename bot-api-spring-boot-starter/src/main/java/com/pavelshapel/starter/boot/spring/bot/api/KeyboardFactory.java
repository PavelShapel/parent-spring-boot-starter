package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.KeyboardButtonContext;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ListenerContext;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class KeyboardFactory<BUTTON extends KeyboardButton, KEYBOARD>
    implements Function<ContextRegistry, KEYBOARD> {

  protected final TreeSet<BUTTON> getApplicableButtons(ContextRegistry contextRegistry) {
    return contextRegistry.get(KeyboardButtonContext.class).keyboardButtons().stream()
        .filter(keyboardButton -> isApplicable(contextRegistry, keyboardButton))
        .map(keyboardButton -> (BUTTON) keyboardButton)
        .collect(Collectors.toCollection(TreeSet::new));
  }

  protected boolean isApplicable(ContextRegistry contextRegistry, KeyboardButton keyboardButton) {
    return keyboardButton.visibleInListenerSimpleNames().isEmpty()
        || keyboardButton
            .visibleInListenerSimpleNames()
            .contains(contextRegistry.get(ListenerContext.class).classSimpleName());
  }
}
