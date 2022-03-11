package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import com.pavelshapel.core.spring.boot.starter.model.Entity;
import org.springframework.core.convert.converter.Converter;

public interface FromDtoConverter<ID, S extends Dto<ID>, T extends Entity<ID>> extends Converter<S, T> {
}
