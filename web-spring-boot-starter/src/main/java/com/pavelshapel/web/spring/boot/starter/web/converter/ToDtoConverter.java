package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public abstract class ToDtoConverter<S extends AbstractEntity, T extends AbstractDto> implements Converter<S, T> {
}
