package com.pavelshapel.core.spring.boot.starter.util;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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

    public <T extends Enum<?>> Optional<T> getRandomisedEnum(Class<T> enumClass) {
        return Optional.ofNullable(enumClass.getEnumConstants())
                .map(enums -> enums.length)
                .filter(length -> length > 0)
                .map(length -> enumClass.getEnumConstants()[getRandomInteger(length)]);
    }

    private int getRandomInteger(int length) {
        return ThreadLocalRandom.current().nextInt(0, length - 1);
    }
}
