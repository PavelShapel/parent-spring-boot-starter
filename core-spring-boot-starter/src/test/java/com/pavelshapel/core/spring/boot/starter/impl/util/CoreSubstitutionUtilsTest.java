package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionProperties;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreSubstitutionUtilsTest {
    public static final String NULL = "null";
    public static final String HELLO = "Hello";
    public static final String WHO = "who";
    public static final String WORLD = "world";
    public static final String SOURCE = String.format("%s ${%s}!", HELLO, WHO);
    public static final SubstitutionProperties PROPERTIES = new SubstitutionProperties(singletonMap(WHO, WORLD));
    public static final String SOURCE_TXT = "source.txt";

    @Autowired
    private SubstitutionUtils coreSubstitutionUtils;

    @Test
    void replace_WithValidParameters_ShouldReturnFilledInString() {
        String result = coreSubstitutionUtils.replace(SOURCE, PROPERTIES);

        assertThat(result).contains(HELLO, WORLD);
    }

    @Test
    void replace_WithNullProperties_ShouldThrowException() {
        assertThatThrownBy(() -> coreSubstitutionUtils.replace(SOURCE, null))
                .hasMessageContainingAll(SOURCE, NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SneakyThrows
    @Test
    void replace_WithValidParameters_ShouldReturnFilledInString(@TempDir Path tempDir) {
        Path templatePath = tempDir.resolve(SOURCE_TXT);
        Files.write(templatePath, singleton(SOURCE));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            String result = coreSubstitutionUtils.replace(inputStream, PROPERTIES);

            assertThat(result).contains(HELLO, WORLD);
        }
    }
}