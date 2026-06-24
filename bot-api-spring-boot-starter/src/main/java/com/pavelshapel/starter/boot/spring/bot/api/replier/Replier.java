package com.pavelshapel.starter.boot.spring.bot.api.replier;

import com.pavelshapel.starter.boot.spring.bot.api.ClientService;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import com.pavelshapel.starter.boot.spring.ordered.OrderedComponent;

public abstract class Replier<PAYLOAD, CLIENT extends ClientService<?, ?, ?>>
    extends OrderedComponent<PAYLOAD, String> {
  private final CLIENT clientService;

  protected Replier(CLIENT clientService) {
    this.clientService = clientService;
  }

  protected final CLIENT getClientService() {
    return clientService;
  }

  @Override
  public final String apply(PAYLOAD payload) {
    return getClass().getSimpleName();
  }

  public abstract void reply(ContextRegistry contextRegistry);

  public final void delete(ContextRegistry contextRegistry) {
    getClientService().deleteMessage(contextRegistry);
  }
}
