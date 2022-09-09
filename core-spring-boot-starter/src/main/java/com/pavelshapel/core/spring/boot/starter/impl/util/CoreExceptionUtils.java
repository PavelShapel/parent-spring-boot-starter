package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CoreExceptionUtils implements ExceptionUtils {
    @Override
    public RuntimeException createIllegalArgumentException(Object... arguments) {
        verifyArguments(arguments);
        return Optional.of(joinArguments(arguments))
                .filter(StringUtils::isNotEmpty)
                .map(joinArguments -> String.format(EXCEPTION_MESSAGE_PATTERN, joinArguments))
                .map(IllegalArgumentException::new)
                .orElseGet(IllegalArgumentException::new);
    }

    private void verifyArguments(Object... arguments) {
        Optional.of(arguments)
                .map(array -> array.length)
                .filter(not(this::isEven))
                .ifPresent(this::throwIllegalArgumentException);
    }

    private void throwIllegalArgumentException(int length) {
        throw createIllegalArgumentException(ARGUMENTS_LENGTH, length);
    }

    private String joinArguments(Object... arguments) {
        return Optional.ofNullable(arguments)
                .map(Arrays::asList)
                .filter(not(List::isEmpty))
                .map(objects -> IntStream.range(0, objects.size()))
                .map(stream -> stream.filter(this::isEven))
                .map(stream -> stream.mapToObj(index -> argumentToString(arguments[index], arguments[index + 1])))
                .map(stream -> stream.collect(Collectors.joining(", ")))
                .orElse(EMPTY);
    }

    private String argumentToString(Object key, Object value) {
        return String.format(
                "%s [%s]",
                getVerifiedArgument(key, KEY),
                getVerifiedArgument(value, VALUE)
        );
    }

    private String getVerifiedArgument(Object argument, String caption) {
        return Optional.ofNullable(argument)
                .map(Object::toString)
                .orElseThrow(() -> createIllegalArgumentException(caption, NULL));
    }

    private boolean isEven(Integer number) {
        return number % 2 == 0;
    }
}
