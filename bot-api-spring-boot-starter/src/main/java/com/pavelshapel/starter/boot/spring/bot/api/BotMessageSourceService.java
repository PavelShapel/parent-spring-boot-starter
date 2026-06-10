package com.pavelshapel.starter.boot.spring.bot.api;

import static java.util.Locale.ENGLISH;

import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.UserContext;
import com.pavelshapel.starter.boot.spring.i18n.MessageSourceService;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;

public final class BotMessageSourceService extends MessageSourceService<ContextRegistry> {
  BotMessageSourceService(MessageSource messageSource) {
    super(messageSource);
  }

  @Override
  protected Locale getLocale(ContextRegistry contextRegistry) {
    return Optional.of(contextRegistry.get(UserContext.class))
        .map(UserContext::languageCode)
        .map(Locale::forLanguageTag)
        .orElse(ENGLISH);
  }
}
