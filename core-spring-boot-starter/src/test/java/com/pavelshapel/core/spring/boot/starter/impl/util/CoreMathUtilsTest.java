package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import com.pavelshapel.core.spring.boot.starter.impl.model.properties.NumberProperties;
import com.pavelshapel.core.spring.boot.starter.impl.model.properties.StringProperties;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CoreMathUtilsTest {
    private static final String RAW_EXPRESSION = "x+(y-2)*z";
    private static final String EXPRESSION = "1.5+(7.25-2.45)*3.333";
    private static final BigDecimal EXPRESSION_RESULT = toScaledBigDecimal(17.50);
    private static final NumberProperties NUMBER_PROPERTIES = new NumberProperties(Map.of(
            "x", 1.5,
            "y", 7.25,
            "z", 3.333
    ));

    private static final StringProperties STRING_PROPERTIES = new StringProperties(NUMBER_PROPERTIES.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().toString()
            )));

    @Mock
    SubstitutionUtils substitutionUtils;
    @InjectMocks
    CoreMathUtils mathUtils;

    @Test
    void evaluate_WithValidParameters_ShouldReturnResult() {
        doReturn(EXPRESSION).when(substitutionUtils).replace(RAW_EXPRESSION, STRING_PROPERTIES);

        BigDecimal result = mathUtils.evaluate(RAW_EXPRESSION, NUMBER_PROPERTIES);

        assertThat(result).isEqualTo(EXPRESSION_RESULT);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullRawExpressionAsParameter_ShouldThrowException(String rawExpression) {
        assertThatThrownBy(() -> mathUtils.evaluate(rawExpression, NUMBER_PROPERTIES))
                .isInstanceOf(NoSuchElementException.class);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullPropertiesAsParameter_ShouldThrowException(NumberProperties properties) {
        assertThatThrownBy(() -> mathUtils.evaluate(RAW_EXPRESSION, properties))
                .isInstanceOf(NoSuchElementException.class);
    }
}