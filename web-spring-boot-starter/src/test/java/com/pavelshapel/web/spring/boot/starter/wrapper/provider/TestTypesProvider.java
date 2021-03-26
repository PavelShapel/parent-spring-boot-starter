package com.pavelshapel.web.spring.boot.starter.wrapper.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.pavelshapel.web.spring.boot.starter.wrapper.provider.TestTypes.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestTypesProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                arguments(STRING.name()),
                arguments(BOOLEAN.name()),
                arguments(LONG.name()),
                arguments(DATE.name()),
                arguments(DOUBLE.name())
        );
    }
}