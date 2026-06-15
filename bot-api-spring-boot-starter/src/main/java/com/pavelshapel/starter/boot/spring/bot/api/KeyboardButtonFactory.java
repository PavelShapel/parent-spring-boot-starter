package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.KeyboardButton;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ListenerContext;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponent;
import java.util.Set;

public abstract class KeyboardButtonFactory<T extends KeyboardButton>
    extends OrderedComponent<ContextRegistry, T> {
  protected static final String MONEY_BAG = "💰";
  protected static final String TRIANGLE_UP = "🔼";
  protected static final String TRIANGLE_DOWN = "🔽";
  protected static final String BACK = "↩️";
  protected static final String INFO = "ℹ️";
  protected static final String RED_CIRCLE = "🔴";
  protected static final String GREEN_CIRCLE = "🟢";
  protected static final String WHITE_CIRCLE = "⚪️";
  protected static final String APPLY = "✅";
  protected static final String CANCEL = "❌";
  protected static final String EDIT = "✏️";
  protected static final String COPY = "📋";
  protected static final String DELETE = "🗑️";
  protected static final String ADD = "➕";
  protected static final String REMOVE = "➖";
  protected static final String BLOCKED = "⛔";
  protected static final String FORBIDDEN = "🚫";
  protected static final String LOCKED = "🔒";

  private final BotMessageSourceService botMessageSourceService;

  protected KeyboardButtonFactory(BotMessageSourceService botMessageSourceService) {
    this.botMessageSourceService = botMessageSourceService;
  }

  protected final String getMessageFromSource(
      ContextRegistry contextRegistry, String key, Object... args) {
    return botMessageSourceService.get(contextRegistry, key, args);
  }

  @Override
  protected final boolean isApplicable(ContextRegistry contextRegistry) {
    return forListenerClassSimpleNames()
            .contains(contextRegistry.get(ListenerContext.class).classSimpleName())
        || forListenerClassSimpleNames().isEmpty();
  }

  protected Set<String> forListenerClassSimpleNames() {
    return Set.of();
  }
}
