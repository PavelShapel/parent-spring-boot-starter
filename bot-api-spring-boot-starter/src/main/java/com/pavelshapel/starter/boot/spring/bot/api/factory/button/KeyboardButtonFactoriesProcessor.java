package com.pavelshapel.starter.boot.spring.bot.api.factory.button;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponentsProcessor;
import java.util.List;
import java.util.TreeSet;

public abstract class KeyboardButtonFactoriesProcessor<
        BUTTON extends KeyboardButton, BUTTON_FACTORY extends KeyboardButtonFactory<BUTTON>>
    extends OrderedComponentsProcessor<ContextRegistry, BUTTON, BUTTON_FACTORY> {

  protected KeyboardButtonFactoriesProcessor(List<BUTTON_FACTORY> keyboardButtonFactories) {
    super(keyboardButtonFactories);
  }

  public final TreeSet<BUTTON> getApplicableButtons(ContextRegistry contextRegistry) {
    return new TreeSet<>(apply(contextRegistry));
  }
}
