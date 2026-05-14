package com.pavelshapel.i18n.spring.boot.starter;

import java.util.Locale;
import org.springframework.context.MessageSource;

public abstract class MessageSourceService<P> {
  private final MessageSource messageSource;

  protected MessageSourceService(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  protected final String get(P payload, String code, Object... args) {
    return messageSource.getMessage(code, args, getLocale(payload));
  }

  protected abstract Locale getLocale(P payload);
}
