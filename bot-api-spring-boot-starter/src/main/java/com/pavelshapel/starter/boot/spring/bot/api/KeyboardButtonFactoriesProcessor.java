package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponentsProcessor;
import java.util.List;
import java.util.TreeSet;

public abstract class KeyboardButtonFactoriesProcessor<
        B extends KeyboardButton, F extends KeyboardButtonFactory<B>>
    extends OrderedComponentsProcessor<ContextRegistry, B, F> {

  protected KeyboardButtonFactoriesProcessor(List<F> keyboardButtonFactories) {
    super(keyboardButtonFactories);
  }

  public final TreeSet<B> getApplicableButtons(ContextRegistry contextRegistry) {
    return new TreeSet<>(apply(contextRegistry));
  }
}
