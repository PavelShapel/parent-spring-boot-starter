package com.pavelshapel.web.spring.boot.starter.controller.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public abstract class DtoConverter<T extends AbstractEntity> implements Converter<T, T> {
    @Override
    public T convert(T source) {
        return source;
    }
}
