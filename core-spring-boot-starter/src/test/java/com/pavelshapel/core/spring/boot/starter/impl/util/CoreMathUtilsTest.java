package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
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

import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CoreMathUtilsTest {
    private static final String RAW_EXPRESSION = "x+(y-2)*z";
    private static final String EXPRESSION = "1.5+(7.25-2.45)*3.333";
    private static final BigDecimal EXPRESSION_RESULT = toScaledBigDecimal(17.50);
    private static final StringProperties PROPERTIES = new StringProperties(Map.of(
            "x", "1.5",
            "y", "7.25",
            "z", "3.333"
    ));

    @Mock
    ExceptionUtils exceptionUtils;
    @Mock
    SubstitutionUtils substitutionUtils;
    @InjectMocks
    CoreMathUtils mathUtils;

    @Test
    void evaluate_WithValidParameters_ShouldReturnResult() {
        doReturn(EXPRESSION).when(substitutionUtils).replace(RAW_EXPRESSION, PROPERTIES);

        BigDecimal result = mathUtils.evaluate(RAW_EXPRESSION, PROPERTIES);

        assertThat(result).isEqualTo(EXPRESSION_RESULT);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullRawExpressionAsParameter_ShouldThrowException(String rawExpression) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> mathUtils.evaluate(rawExpression, PROPERTIES))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void evaluate_WithNullPropertiesAsParameter_ShouldThrowException(StringProperties properties) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> mathUtils.evaluate(RAW_EXPRESSION, properties))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ThrowableNotThrown")
    private void mockExceptionUtilsCreateIllegalArgumentException() {
        doReturn(new IllegalArgumentException()).when(exceptionUtils).createIllegalArgumentException(any());
    }
}