package com.pavelshapel.random.spring.boot.starter.randomizer.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultRangesTest {
    @ParameterizedTest
    @EnumSource(DefaultRanges.class)
    void toString_ShouldReturnStringWithAppropriateValue(DefaultRanges defaultRanges) {
        String result = defaultRanges.toString();

        assertThat(result)
                .isNotEmpty()
                .isEqualTo(String.format(
                        "DefaultRanges.%s(value=[%d..%d])",
                        defaultRanges.name(),
                        defaultRanges.getValue().getMinimum(),
                        defaultRanges.getValue().getMaximum()
                ));
    }
}