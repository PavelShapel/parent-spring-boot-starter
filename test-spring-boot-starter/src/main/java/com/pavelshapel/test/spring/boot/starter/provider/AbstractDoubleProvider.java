package com.pavelshapel.test.spring.boot.starter.provider;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractDoubleProvider extends AbstractProvider {
    protected AbstractDoubleProvider(int argumentsCount) {
        super(argumentsCount);
    }

    @Override
    protected Object[] getArguments(int argumentsCount) {
        return ThreadLocalRandom.current().doubles(argumentsCount)
                .boxed()
                .toArray();
    }
}