package com.pavelshapel.random.spring.boot.starter.randomizer.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class BoundTest {
    @ParameterizedTest
    @EnumSource(Bound.class)
    void toString_ShouldReturnStringWithAppropriateValue(Bound bound) {
        String result = bound.toString();

        assertThat(result)
                .isNotEmpty()
                .isEqualTo(String.format(
                        "Bound.%s(value=%d)",
                        bound.name(),
                        bound.getValue()
                ));
    }
}