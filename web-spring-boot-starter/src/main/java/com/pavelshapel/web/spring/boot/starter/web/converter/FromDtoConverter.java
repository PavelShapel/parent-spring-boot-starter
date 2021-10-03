package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public interface FromDtoConverter<S extends AbstractDto, T extends AbstractEntity> extends Converter<S, T> {
}
