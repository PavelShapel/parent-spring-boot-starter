package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public interface ToDtoConverter<S extends AbstractEntity, T extends AbstractDto> extends Converter<S, T> {
}
