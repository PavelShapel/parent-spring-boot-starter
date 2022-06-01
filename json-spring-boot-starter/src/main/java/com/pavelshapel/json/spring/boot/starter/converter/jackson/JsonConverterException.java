package com.pavelshapel.json.spring.boot.starter.converter.jackson;

public class JsonConverterException extends IllegalArgumentException {
    public static final String ILLEGAL_ARGUMENT_PATTERN = "illegal argument(s) [%s]";

    public JsonConverterException(String... arguments) {
        super(buildMessage(arguments));
    }

    public JsonConverterException(Throwable cause, String... arguments) {
        super(buildMessage(arguments), cause);
    }

    private static String buildMessage(String[] arguments) {
        return String.format(ILLEGAL_ARGUMENT_PATTERN, joinArguments(arguments));
    }

    private static String joinArguments(String... arguments) {
        return String.join(", ", arguments);
    }
}
