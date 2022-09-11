package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.StringProperties;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CoreSubstitutionUtils implements SubstitutionUtils {
    @Override
    public String replace(InputStream source, StringProperties properties) {
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(source)).lines()) {
            String template = lines.collect(Collectors.joining(System.lineSeparator()));
            return replace(template, properties);
        }
    }

    @Override
    public String replace(String source, StringProperties properties) {
        return StringSubstitutor.replace(source, properties);
    }

    @Override
    public String replace(String source, String... properties) {
        StringProperties stringProperties = IntStream.range(0, properties.length)
                .boxed()
                .collect(Collectors.toMap(
                        String::valueOf,
                        index -> properties[index],
                        (existing, replacement) -> existing,
                        StringProperties::new)
                );
        return replace(source, stringProperties);
    }
}
