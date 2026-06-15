package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.SocialType;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import java.util.Optional;

public interface PublicService {
  Optional<ContextRegistry> findBySocialIdAndSocialTypeAndMapToContextRegistry(
      Long socialId, SocialType socialType);
}
