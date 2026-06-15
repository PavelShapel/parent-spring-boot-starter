package com.pavelshapel.starter.boot.spring.bot.api.model;

import java.util.Comparator;
import org.jspecify.annotations.NonNull;

public interface KeyboardButton extends Comparable<KeyboardButton> {
  int row();

  int col();

  @Override
  default int compareTo(@NonNull KeyboardButton other) {
    return Comparator.comparingInt(KeyboardButton::row)
        .thenComparingInt(KeyboardButton::col)
        .compare(this, other);
  }
}
