package com.pavelshapel.starter.boot.spring.bot.api.service;

import com.pavelshapel.starter.boot.spring.api.common.BiConverter;
import com.pavelshapel.starter.boot.spring.bot.api.model.SocialType;
import com.pavelshapel.starter.boot.spring.bot.api.model.context.ContextRegistry;
import java.util.Optional;

public abstract class DaoService<ENTITY, CONVERTER extends BiConverter<ContextRegistry, ENTITY>>
    implements PublicService {
  private final CONVERTER converter;

  protected DaoService(CONVERTER converter) {
    this.converter = converter;
  }

  protected abstract void save(ENTITY entity);

  protected abstract Optional<ENTITY> findBySocialIdAndSocialType(
      Long socialId, SocialType socialType);

  protected abstract boolean existsBySocialIdAndSocialType(Long socialId, SocialType socialType);

  @Override
  public final Optional<ContextRegistry> findBySocialIdAndSocialTypeAndMapToContextRegistry(
      Long socialId, SocialType socialType) {
    return findBySocialIdAndSocialType(socialId, socialType).map(converter::convertBackward);
  }
}
