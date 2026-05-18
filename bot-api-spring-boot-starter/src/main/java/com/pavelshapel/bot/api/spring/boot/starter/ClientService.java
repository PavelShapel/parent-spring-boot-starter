package com.pavelshapel.bot.api.spring.boot.starter;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.ContextRegistry;

public interface ClientService {

  void sendMessage(ContextRegistry contextRegistry, String text);

  void editMessage(ContextRegistry contextRegistry, String text);

  void deleteMessage(ContextRegistry contextRegistry);
}
