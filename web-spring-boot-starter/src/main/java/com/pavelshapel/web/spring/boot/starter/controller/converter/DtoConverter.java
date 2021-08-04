package com.pavelshapel.web.spring.boot.starter.controller.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.core.convert.converter.Converter;

public interface DtoConverter<T extends AbstractEntity> extends Converter<T, T> {
    @Override
    default T convert(T source) {
        return source;
    }
}
