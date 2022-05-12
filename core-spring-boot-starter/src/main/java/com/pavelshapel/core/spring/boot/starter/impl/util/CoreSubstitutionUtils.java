package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionProperties;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class CoreSubstitutionUtils implements SubstitutionUtils {
    public static final String MESSAGE_PATTERN = "template [%s], properties [%s]";

    @Override
    public String replace(InputStream source, SubstitutionProperties properties) {
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(source)).lines()) {
            String template = lines.collect(Collectors.joining(System.lineSeparator()));
            return replace(template, properties);
        }
    }

    @Override
    public String replace(String source, SubstitutionProperties properties) {
        if (isNull(source) || isNull(properties)) {
            throw createIllegalArgumentException(source, properties);
        }
        return new StringSubstitutor(properties).replace(source);
    }

    private IllegalArgumentException createIllegalArgumentException(String template, SubstitutionProperties properties) {
        String message = String.format(MESSAGE_PATTERN, template, properties);
        return new IllegalArgumentException(message);
    }
}
