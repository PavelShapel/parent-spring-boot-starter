package com.pavelshapel.core.spring.boot.starter.api.util;

import java.util.Map;

public interface ExceptionUtils {
    String EXCEPTION_MESSAGE_PATTERN = "illegal argument(s): %s";

    RuntimeException createIllegalArgumentException(Map<String, Object> arguments);
}
