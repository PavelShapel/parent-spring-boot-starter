package com.pavelshapel.core.spring.boot.starter.impl.converter;

import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractConverter<S, T> implements Converter<S, T> {
    @Autowired
    ClassUtils classUtils;

    @Override
    public T convert(@NonNull S source) {
        T target = classUtils.getGenericSuperclass(getClass(), 2)
                .map(this::createNewInstance)
                .orElseThrow(IllegalAccessError::new);
        classUtils.copyFields(source, target);
        return target;
    }

    @SneakyThrows
    private T createNewInstance(Class<?> targetClass) {
        return (T) targetClass.getDeclaredConstructor().newInstance();
    }
}
