package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionProperties;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class CoreSubstitutionUtils implements SubstitutionUtils {
    public static final String MESSAGE_PATTERN = "template [%s], properties [%s]";

    @Override
    public String replace(InputStream source, SubstitutionProperties properties) {
        Optional<String> template = Optional.ofNullable(source)
                .map(InputStreamReader::new)
                .map(BufferedReader::new)
                .map(BufferedReader::lines)
                .map(stream -> stream.collect(Collectors.joining(System.lineSeparator())));
        return replace(template.orElse(null), properties);
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
