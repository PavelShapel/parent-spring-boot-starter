package com.pavelshapel.core.spring.boot.starter.util.impl;

import com.pavelshapel.core.spring.boot.starter.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class CoreClassUtils implements ClassUtils {
    @Override
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass) {
        return getGenericSuperclass(sourceClass, 0);
    }

    @Override
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass, int index) {
        try {
            return Optional.ofNullable(sourceClass)
                    .map(Class::getGenericSuperclass)
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .map(ParameterizedType::getActualTypeArguments)
                    .map(types -> types[index])
                    .map(type -> (Class<?>) type);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
