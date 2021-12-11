package com.pavelshapel.core.spring.boot.starter.util;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class CommonUtils {
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass) {
        return getGenericSuperclass(sourceClass, 0);
    }

    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass, int index) {
        try {
            return Optional.of((Class<?>) ((ParameterizedType) sourceClass.getGenericSuperclass()).getActualTypeArguments()[index]);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
