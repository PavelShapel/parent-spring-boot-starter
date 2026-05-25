package com.pavelshapel.bot.telegram.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.ClientService;
import com.pavelshapel.bot.api.spring.boot.starter.Listener;

public abstract class TelegramListener extends Listener<TelegramClientService> {
  protected TelegramListener(ClientService clientService) {
    super((TelegramClientService) clientService);
  }
}
