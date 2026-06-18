package com.pavelshapel.starter.boot.spring.bot.api.model.context;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import java.util.TreeSet;

public record KeyboardButtonContext(TreeSet<KeyboardButton> keyboardButtons) implements Context {
  public KeyboardButtonContext withButton(KeyboardButton keyboardButton) {
    keyboardButtons.add(keyboardButton);
    return this;
  }
}
