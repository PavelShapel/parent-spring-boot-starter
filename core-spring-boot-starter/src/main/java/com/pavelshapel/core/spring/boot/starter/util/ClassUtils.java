package com.pavelshapel.core.spring.boot.starter.util;

import java.util.Optional;

public interface ClassUtils {
    Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass);

    Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass, int index);
}
