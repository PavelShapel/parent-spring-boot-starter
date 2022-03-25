package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.RandomUtils;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class CoreRandomUtils implements RandomUtils {
    @Override
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
