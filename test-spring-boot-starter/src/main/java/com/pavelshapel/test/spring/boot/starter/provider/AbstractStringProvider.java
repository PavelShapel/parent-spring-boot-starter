package com.pavelshapel.test.spring.boot.starter.provider;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.stream.Stream;

public abstract class AbstractStringProvider extends AbstractProvider {
    protected AbstractStringProvider(int argumentsCount) {
        super(argumentsCount);
    }

    @Override
    protected Object[] getArguments(int argumentsCount) {
        return Stream.generate(() -> RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE))
                .limit(argumentsCount)
                .toArray();
    }
}