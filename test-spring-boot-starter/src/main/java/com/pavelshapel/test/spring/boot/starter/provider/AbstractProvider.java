package com.pavelshapel.test.spring.boot.starter.provider;

import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public abstract class AbstractProvider implements ArgumentsProvider {
    public static final int DEFAULT_ITERATIONS_COUNT = 10;
    public static final String EXCEPTION_MESSAGE_PATTERN = "not found implementation for [%s]";

    private final int iterationsCount;
    private final Class<?>[] classes;

    protected AbstractProvider(int iterationsCount, Class<?>... classes) {
        this.iterationsCount = iterationsCount;
        this.classes = classes;
    }

    protected AbstractProvider(Class<?>... classes) {
        this(DEFAULT_ITERATIONS_COUNT, classes);
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
        return Arrays.stream(PrimitiveType.values())
                .map(PrimitiveType::getRandomValueSupplier)
                .map(Supplier::get)
                .filter(targetClass::isInstance)
                .findFirst()
                .orElseThrow(() -> createNotImplementedException(targetClass));
    }

    private NotImplementedException createNotImplementedException(Class<?> targetClass) {
        return new NotImplementedException(String.format(EXCEPTION_MESSAGE_PATTERN, targetClass.getSimpleName()));
    }
}