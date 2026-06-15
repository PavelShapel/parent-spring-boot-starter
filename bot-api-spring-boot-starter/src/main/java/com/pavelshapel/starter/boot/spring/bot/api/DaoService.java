package com.pavelshapel.starter.boot.spring.bot.api;

import com.pavelshapel.starter.boot.spring.bot.api.model.SocialType;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import java.util.Optional;

public abstract class DaoService<E, C extends BiConverter<ContextRegistry, E>>
    implements PublicService {
  private final C chatConverter;

  protected DaoService(C chatConverter) {
    this.chatConverter = chatConverter;
  }

  protected abstract void save(E entity);

  protected abstract Optional<E> findBySocialIdAndSocialType(Long socialId, SocialType socialType);

  protected abstract boolean existsBySocialIdAndSocialType(Long socialId, SocialType socialType);

  @Override
  public final Optional<ContextRegistry> findBySocialIdAndSocialTypeAndMapToContextRegistry(
      Long socialId, SocialType socialType) {
    return findBySocialIdAndSocialType(socialId, socialType).map(chatConverter::convertBackward);
  }
}
