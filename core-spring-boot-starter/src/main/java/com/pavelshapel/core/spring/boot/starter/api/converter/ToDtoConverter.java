package com.pavelshapel.core.spring.boot.starter.api.converter;

import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.core.convert.converter.Converter;

public interface ToDtoConverter<ID, S extends Entity<ID>, T extends Dto<ID>> extends Converter<S, T> {
}
