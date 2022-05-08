package com.pavelshapel.core.spring.boot.starter.impl.converter;

import com.pavelshapel.core.spring.boot.starter.api.converter.ToDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;

public abstract class AbstractToDtoConverter<ID, E extends Entity<ID>, D extends Dto<ID>> extends AbstractConverter<E, D> implements ToDtoConverter<ID, E, D> {
}
