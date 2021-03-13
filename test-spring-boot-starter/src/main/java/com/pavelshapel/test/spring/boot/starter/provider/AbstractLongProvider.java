package com.pavelshapel.test.spring.boot.starter.provider;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractLongProvider extends AbstractProvider {
    protected AbstractLongProvider(int argumentsCount) {
        super(argumentsCount);
    }

    @Override
    protected Object[] getArguments(int argumentsCount) {
        return ThreadLocalRandom.current().longs(argumentsCount)
                .boxed()
                .toArray();
    }
}