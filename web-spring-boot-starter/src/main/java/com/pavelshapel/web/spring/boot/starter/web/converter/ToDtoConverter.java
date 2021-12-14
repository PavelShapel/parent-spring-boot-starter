package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import com.pavelshapel.web.spring.boot.starter.web.dto.Dto;
import org.springframework.core.convert.converter.Converter;

public interface ToDtoConverter<ID, S extends Entity<ID>, T extends Dto<ID>> extends Converter<S, T> {
}
