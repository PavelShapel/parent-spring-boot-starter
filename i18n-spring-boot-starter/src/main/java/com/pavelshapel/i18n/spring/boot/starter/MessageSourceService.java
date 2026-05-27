package com.pavelshapel.i18n.spring.boot.starter;

import static java.util.Locale.ENGLISH;

import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;

public abstract class MessageSourceService<P> {
  private final MessageSource messageSource;

  protected MessageSourceService(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public final String get(P payload, String key, Object... args) {
    return messageSource.getMessage(key, args, getLocale(payload));
  }

  public final String get(String languageCode, String key, Object... args) {
    return messageSource.getMessage(key, args, getLocale(languageCode));
  }

  protected abstract Locale getLocale(P payload);

  private static Locale getLocale(String languageCode) {
    return Optional.ofNullable(languageCode).map(Locale::forLanguageTag).orElse(ENGLISH);
  }
}
