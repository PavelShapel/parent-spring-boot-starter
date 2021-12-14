package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import com.pavelshapel.web.spring.boot.starter.web.dto.Dto;
import org.springframework.core.convert.converter.Converter;

public interface FromDtoConverter<ID, S extends Dto<ID>, T extends Entity<ID>> extends Converter<S, T> {
}
