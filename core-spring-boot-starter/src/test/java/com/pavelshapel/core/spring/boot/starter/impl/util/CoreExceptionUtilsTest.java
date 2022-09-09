package com.pavelshapel.core.spring.boot.starter.impl.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ThrowableNotThrown")
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CoreExceptionUtilsTest {
    @Spy
    CoreExceptionUtils exceptionUtils;

    @Test
    void createIllegalArgumentException_WithValidParameters_ShouldCreateExceptionWithMessage() {
        RuntimeException result = exceptionUtils.createIllegalArgumentException(KEY, VALUE);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(EXCEPTION_MESSAGE_PATTERN, String.format("%s [%s]", KEY, VALUE)));
    }

    @Test
    void createIllegalArgumentException_WithOddParametersLength_ShouldThrowException() {
        assertThatThrownBy(() -> exceptionUtils.createIllegalArgumentException(KEY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(EXCEPTION_MESSAGE_PATTERN, String.format("%s [%d]", ARGUMENTS_LENGTH, 1)));
    }

    @Test
    void createIllegalArgumentException_WithEmptyParameter_ShouldCreateExceptionWithoutMessage() {
        RuntimeException result = exceptionUtils.createIllegalArgumentException();

        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .isNull();
    }

    @Test
    void createIllegalArgumentException_WithNullKeyParameter_ShouldThrowException() {
        assertThatThrownBy(() -> exceptionUtils.createIllegalArgumentException(null, VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(EXCEPTION_MESSAGE_PATTERN, String.format("%s [null]", KEY)));
    }

    @Test
    void createIllegalArgumentException_WithNullValueParameter_ShouldThrowException() {
        assertThatThrownBy(() -> exceptionUtils.createIllegalArgumentException(KEY, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(EXCEPTION_MESSAGE_PATTERN, String.format("%s [null]", VALUE)));
    }
}