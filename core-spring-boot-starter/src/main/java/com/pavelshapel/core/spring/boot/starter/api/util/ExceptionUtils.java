package com.pavelshapel.core.spring.boot.starter.api.util;

import java.util.Map;

public interface ExceptionUtils {
    String EXCEPTION_MESSAGE_PATTERN = "illegal argument(s): %s";

    Exception createIllegalArgumentException(Map<String, String> arguments);
}
