package com.pavelshapel.core.spring.boot.starter.api.util;

public interface ExceptionUtils {
    String EXCEPTION_MESSAGE_PATTERN = "illegal argument(s): %s";
    String NULL = "null";
    String ARGUMENTS_LENGTH = "arguments.length";
    String KEY = "key";
    String VALUE = "value";

    RuntimeException createIllegalArgumentException(Object... arguments);
}
