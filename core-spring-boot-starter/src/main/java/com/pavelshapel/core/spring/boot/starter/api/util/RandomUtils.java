package com.pavelshapel.core.spring.boot.starter.api.util;

import java.util.Optional;

public interface RandomUtils {
    <T extends Enum<?>> Optional<T> getRandomisedEnum(Class<T> enumClass);
}
