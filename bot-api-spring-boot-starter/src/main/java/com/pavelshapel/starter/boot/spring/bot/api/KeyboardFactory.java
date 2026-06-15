package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import java.util.TreeSet;
import java.util.function.Function;

public abstract class KeyboardFactory<
        B extends KeyboardButton,
        F extends KeyboardButtonFactory<B>,
        P extends KeyboardButtonFactoriesProcessor<B, F>,
        K>
    implements Function<ContextRegistry, K> {
  private final P keyboardButtonFactoriesProcessor;

  protected KeyboardFactory(P keyboardButtonFactoriesProcessor) {
    this.keyboardButtonFactoriesProcessor = keyboardButtonFactoriesProcessor;
  }

  protected final TreeSet<B> getApplicableButtons(ContextRegistry contextRegistry) {
    return keyboardButtonFactoriesProcessor.getApplicableButtons(contextRegistry);
  }
}
