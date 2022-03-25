package com.pavelshapel.core.spring.boot.starter.api.converter;

import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.core.convert.converter.Converter;

public interface FromDtoConverter<ID, S extends Dto<ID>, T extends Entity<ID>> extends Converter<S, T> {
}
