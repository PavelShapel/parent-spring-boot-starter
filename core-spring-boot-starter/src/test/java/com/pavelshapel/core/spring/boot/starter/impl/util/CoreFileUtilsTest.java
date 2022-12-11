package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.util.FileUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreFileUtilsTest {
    private static final String SOURCE_TXT = "source.txt";
    private static final String SOURCE = "source";

    @Autowired
    private FileUtils fileUtils;

    @Test
    @SneakyThrows
    void getContentType_WithValidTextFileAsParameter_ShouldReturnAppropriateContentTypeName(@TempDir Path tempDir) {
        Path templatePath = tempDir.resolve(SOURCE_TXT);
        Files.write(templatePath, singleton(SOURCE));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            String result = fileUtils.getContentType(inputStream);

            assertThat(result).contains(TEXT_PLAIN_VALUE);
        }
    }
}