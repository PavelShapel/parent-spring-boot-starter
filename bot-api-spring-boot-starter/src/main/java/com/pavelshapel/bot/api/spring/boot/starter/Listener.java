package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;
import java.util.function.Consumer;

public abstract class Listener<C extends ClientService> implements Consumer<ContextRegistry> {
  public static final String DOT_IS_APPLICABLE_SIGNATURE = ".isApplicable(#contextRegistry)";

  private final C clientService;

  protected Listener(C clientService) {
    this.clientService = clientService;
  }

  protected void sendMessage(ContextRegistry contextRegistry, String text) {
    clientService.sendMessage(contextRegistry, text);
  }

  protected void editMessage(ContextRegistry contextRegistry, String text) {
    clientService.editMessage(contextRegistry, text);
  }

  protected void deleteMessage(ContextRegistry contextRegistry) {
    clientService.deleteMessage(contextRegistry);
  }

  public boolean isApplicable(ContextRegistry contextRegistry) {
    return true;
  }
}
