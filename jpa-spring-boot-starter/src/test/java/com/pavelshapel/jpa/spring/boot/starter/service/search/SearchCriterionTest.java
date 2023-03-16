package com.pavelshapel.jpa.spring.boot.starter.service.search;


import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchCriterionTest {
    @Test
    void getCastedValue_WithValidParameter_ShouldReturnValue() {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .value(String.format("test<%s>", PrimitiveType.STRING.name()))
                .build();

        Comparable<?> result = searchCriterion.getCastedValue();

        assertThat(result)
                .isInstanceOf(String.class)
                .isEqualTo("test");
    }

    @ParameterizedTest
    @NullSource
    void getCastedValue_WithNullValueParameter_ShouldThrowException(String value) {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .value(value)
                .build();

        Comparable<?> result = searchCriterion.getCastedValue();

        assertThat(result)
                .isNull();
    }

    @Test
    void getCastedValue_WithoutTypeInParameter_ShouldThrowException() {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .value("test")
                .build();

        assertThatThrownBy(searchCriterion::getCastedValue)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("type not found [test]");
    }

    @Test
    void getCastedValue_WithoutValueInParameter_ShouldThrowException() {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .value(String.format("null<%s>", PrimitiveType.STRING.name()))
                .build();

        assertThatThrownBy(searchCriterion::getCastedValue)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid value [null<STRING>]");
    }
}