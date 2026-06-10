package com.pavelshapel.starter.boot.spring.bot.api.model;

import java.time.Instant;

public interface Entity {
  Long id();

  long socialId();

  SocialType socialType();

  Instant updatedAt();
}
