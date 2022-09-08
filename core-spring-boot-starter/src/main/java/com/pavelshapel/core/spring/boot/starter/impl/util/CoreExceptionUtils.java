package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CoreExceptionUtils implements ExceptionUtils {
    @Override
    public Exception createIllegalArgumentException(Map<String, String> arguments) {
        return Optional.of(joinArguments(arguments))
                .filter(StringUtils::isNotEmpty)
                .map(joinArguments -> String.format("illegal argument(s): %s", joinArguments))
                .map(IllegalArgumentException::new)
                .orElseGet(IllegalArgumentException::new);
    }

    private String joinArguments(Map<String, String> arguments) {
        return Optional.ofNullable(arguments)
                .map(Map::entrySet)
                .map(Collection::stream)
                .map(stream -> stream.map(this::entryToString))
                .map(stream -> stream.collect(Collectors.joining(", ")))
                .orElse(EMPTY);
    }

    private String entryToString(Map.Entry<String, String> entry) {
        return String.format("%s [%s]", entry.getKey(), entry.getValue());
    }
}
