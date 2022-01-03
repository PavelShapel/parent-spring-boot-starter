package com.pavelshapel.core.spring.boot.starter.util;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class CommonUtils {
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass) {
        return getGenericSuperclass(sourceClass, 0);
    }

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
