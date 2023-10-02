package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.NumberProperties;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CoreMathUtilsTest {
    private static final String RAW_EXPRESSION = "x+(y-2.45)*z";
    private static final BigDecimal EXPRESSION_RESULT = BigDecimal.valueOf(17.4984);
    private static final NumberProperties NUMBER_PROPERTIES = new NumberProperties(Map.of(
            "x", 1.5,
            "y", 7.25,
            "z", 3.333
    ));

    @InjectMocks
    CoreMathUtils mathUtils;

    @Test
    void evaluate_WithValidParameters_ShouldReturnResult() {
        BigDecimal result = mathUtils.evaluate(RAW_EXPRESSION, NUMBER_PROPERTIES);

        assertThat(result).isEqualTo(EXPRESSION_RESULT);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullRawExpressionAsParameter_ShouldThrowException(String rawExpression) {
        assertThatThrownBy(() -> mathUtils.evaluate(rawExpression, NUMBER_PROPERTIES))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullPropertiesAsParameter_ShouldThrowException(NumberProperties properties) {
        assertThatThrownBy(() -> mathUtils.evaluate(RAW_EXPRESSION, properties))
                .isInstanceOf(NullPointerException.class);
    }
}