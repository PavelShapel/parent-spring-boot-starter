package com.pavelshapel.bot.api.spring.boot.starter;

import static java.util.Locale.ENGLISH;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import com.pavelshapel.bot.api.spring.boot.starter.model.context.UserContext;
import com.pavelshapel.i18n.spring.boot.starter.MessageSourceService;
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
