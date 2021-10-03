package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public abstract class FromDtoConverter<S extends AbstractDto, T extends AbstractEntity> implements Converter<S, T> {
}
