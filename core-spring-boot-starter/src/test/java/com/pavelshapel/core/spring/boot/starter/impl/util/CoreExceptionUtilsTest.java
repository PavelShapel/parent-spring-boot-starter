package com.pavelshapel.core.spring.boot.starter.impl.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils.EXCEPTION_MESSAGE_PATTERN;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CoreExceptionUtilsTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final Map<String, String> ARGUMENTS = singletonMap(KEY, VALUE);

    @Spy
    CoreExceptionUtils exceptionUtils;

    @Test
    void createIllegalArgumentException_WithValidParameter_ShouldCreateExceptionWithMessage() {
        Exception result = exceptionUtils.createIllegalArgumentException(ARGUMENTS);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(EXCEPTION_MESSAGE_PATTERN, String.format("%s [%s]", KEY, VALUE)));
    }

    @ParameterizedTest
    @NullSource
    void createIllegalArgumentException_WithNullParameter_ShouldCreateExceptionWithMessage(Map<String, String> arguments) {
        Exception result = exceptionUtils.createIllegalArgumentException(arguments);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .isNull();
    }
}