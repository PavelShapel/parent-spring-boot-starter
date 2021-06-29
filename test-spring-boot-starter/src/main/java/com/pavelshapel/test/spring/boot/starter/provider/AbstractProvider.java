package com.pavelshapel.test.spring.boot.starter.provider;

import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl.GenericRandomizerFactory;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public abstract class AbstractProvider implements ArgumentsProvider {
    private final int iterationsCount;
    private final Class<?>[] classes;

    @Autowired
    private GenericRandomizerFactory genericRandomizerFactory;

    protected AbstractProvider(int iterationsCount, Class<?>... classes) {
        this.iterationsCount = iterationsCount;
        this.classes = classes;
    }

    protected AbstractProvider(Class<?>... classes) {
        this(10, classes);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return provideGenericArguments();
    }

    private Stream<Arguments> provideGenericArguments() {
        return Stream.generate(() -> arguments(getArguments())).limit(this.iterationsCount);
    }

    private Object[] getArguments() {
        return Arrays.stream(classes)
                .map(this::getArgument)
                .toArray();
    }

    private Object getArgument(Class<?> targetClass) {
        return getRandomizer(targetClass).randomize();
    }

    private Randomizer<?> getRandomizer(Class<?> targetClass) {
        return genericRandomizerFactory.getRandomizer(targetClass.getSimpleName());
    }
}