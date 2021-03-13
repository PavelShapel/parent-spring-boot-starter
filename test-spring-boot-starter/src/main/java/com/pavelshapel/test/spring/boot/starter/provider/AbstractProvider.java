package com.pavelshapel.test.spring.boot.starter.provider;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@FieldDefaults(
        makeFinal = true,
        level = AccessLevel.PRIVATE
)
public abstract class AbstractProvider implements ArgumentsProvider {
    int argumentsCount;
    int iterationsCount;

    protected AbstractProvider(int argumentsCount, int iterationsCount) {
        this.argumentsCount = Math.abs(argumentsCount);
        this.iterationsCount = Math.abs(iterationsCount);
    }

    protected AbstractProvider(int argumentsCount) {
        this(argumentsCount, 10);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return provideGenericArguments(argumentsCount, iterationsCount);
    }

    private Stream<Arguments> provideGenericArguments(int argumentsCount, int iterationsCount) {
        return Stream.generate(() -> arguments(getArguments(argumentsCount))).limit(iterationsCount);
    }

    protected abstract Object[] getArguments(int argumentsCount);
}