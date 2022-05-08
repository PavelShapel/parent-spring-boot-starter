package com.pavelshapel.core.spring.boot.starter.impl.converter;

import com.pavelshapel.core.spring.boot.starter.api.converter.FromDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;

public abstract class AbstractFromDtoConverter<ID, D extends Dto<ID>, E extends Entity<ID>> extends AbstractConverter<D, E> implements FromDtoConverter<ID, D, E> {
}
