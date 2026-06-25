package com.pavelshapel.starter.boot.spring.bot.telegram.service.factory.button;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public final class TelegramKeyboardButton extends InlineKeyboardButton implements KeyboardButton {
  private static final String STYLE_PRIMARY = "primary";
  private static final String STYLE_SUCCESS = "success";
  private static final String STYLE_DANGER = "danger";

  private final int row;
  private final int col;
  private final Set<String> visibleInListenerSimpleNames;

  private TelegramKeyboardButton(Builder builder) {
    super(builder.parentBuilder);
    this.row = builder.row;
    this.col = builder.col;
    this.visibleInListenerSimpleNames =
        builder.visibleInListenerSimpleNames != null
            ? Collections.unmodifiableSet(builder.visibleInListenerSimpleNames)
            : Collections.emptySet();
  }

  public static Builder keyboardButtonBuilder() {
    return new Builder();
  }

  @Override
  public int row() {
    return row;
  }

  @Override
  public int col() {
    return col;
  }

  @Override
  public Set<String> visibleInListenerSimpleNames() {
    return visibleInListenerSimpleNames;
  }

  public static class Builder {
    private final InlineKeyboardButtonBuilder<?, ?> parentBuilder = InlineKeyboardButton.builder();
    private int row;
    private int col;
    private Set<String> visibleInListenerSimpleNames;

    public Builder row(int row) {
      this.row = row;
      return this;
    }

    public Builder col(int col) {
      this.col = col;
      return this;
    }

    public Builder visibleInListenerSimpleNames(Set<String> visibleInListenerSimpleNames) {
      this.visibleInListenerSimpleNames = new HashSet<>(visibleInListenerSimpleNames);
      return this;
    }

    public Builder visibleInListenerSimpleName(String visibleInListenerSimpleName) {
      if (this.visibleInListenerSimpleNames == null) {
        this.visibleInListenerSimpleNames = new HashSet<>();
      }
      this.visibleInListenerSimpleNames.add(visibleInListenerSimpleName);
      return this;
    }

    public Builder text(String text) {
      this.parentBuilder.text(text);
      return this;
    }

    public Builder callbackData(String callbackData) {
      this.parentBuilder.callbackData(callbackData);
      return this;
    }

    public Builder url(String url) {
      this.parentBuilder.url(url);
      return this;
    }

    public Builder stylePrimary() {
      this.parentBuilder.style(STYLE_PRIMARY);
      return this;
    }

    public Builder styleSuccess() {
      this.parentBuilder.style(STYLE_SUCCESS);
      return this;
    }

    public Builder styleDanger() {
      this.parentBuilder.style(STYLE_DANGER);
      return this;
    }

    public TelegramKeyboardButton build() {
      return new TelegramKeyboardButton(this);
    }
  }
}
