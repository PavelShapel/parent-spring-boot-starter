package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.StringProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CoreSubstitutionUtilsTest {
    private static final String HELLO = "Hello";
    private static final String WHO = "who";
    private static final String WORLD = "world";
    private static final String SOURCE = HELLO + " ${" + WHO + "}!";
    private static final String SOURCE_WITH_DEFAULT_PROPERTIES = HELLO + " ${0}!";
    private static final StringProperties PROPERTIES = new StringProperties(singletonMap(WHO, WORLD));
    private static final String SOURCE_TXT = "source.txt";

    @Spy
    private CoreSubstitutionUtils substitutionUtils;

    @Test
    void replace_WithValidParameters_ShouldReturnFilledInString() {
        String result = substitutionUtils.replace(SOURCE, PROPERTIES);

        assertThat(result).contains(HELLO, WORLD);
    }

    @Test
    void replace_WithValidDefaultParameters_ShouldReturnFilledInString() {
        String result = substitutionUtils.replace(SOURCE_WITH_DEFAULT_PROPERTIES, WORLD);

        assertThat(result).contains(HELLO, WORLD);
    }

    @SneakyThrows
    @Test
    void replace_WithValidParameters_ShouldReturnFilledInString(@TempDir Path tempDir) {
        Path templatePath = tempDir.resolve(SOURCE_TXT);
        Files.write(templatePath, singleton(SOURCE));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            String result = substitutionUtils.replace(inputStream, PROPERTIES);

            assertThat(result).contains(HELLO, WORLD);
        }
    }
}