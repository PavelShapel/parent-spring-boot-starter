package com.pavelshapel.core.spring.boot.starter.enums;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveTypeTest {
    @Test
    void getRandomValueSupplier_ShouldReturnNotNullValue() {
        Arrays.stream(PrimitiveType.values())
                .map(PrimitiveType::getRandomValueSupplier)
                .map(Supplier::get)
                .forEach(randomValue -> assertThat(randomValue).isNotNull());
    }

    @Test
    void getCastFunction_ShouldReturnNotNullValue() {
        Arrays.stream(PrimitiveType.values())
                .map(PrimitiveType::getCastFunction)
                .forEach(function -> assertThat(function).isNotNull());
    }
}