package com.pavelshapel.stream.spring.boot.starter.util;

import com.pavelshapel.stream.spring.boot.starter.StarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pavelshapel.stream.spring.boot.starter.StarterAutoConfiguration.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        PREFIX + "." + PROPERTY_NAME + "=" + TRUE
})
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class
})
class StreamUtilsTest {
    @Autowired
    private StreamUtils streamUtils;

    @Test
    void toSingleton_WithValidParam_ShouldReturnResult() {
        String message = "singleton";
        final List<Object> list = Collections.singletonList(message);

        final Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isNotEmpty();
    }

    @Test
    void toSingleton_WithEmptyParam_ShouldReturnOptionalEmpty() {
        final List<Object> list = Collections.emptyList();

        final Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }

    @Test
    void toSingleton_WithManyParams_ShouldReturnOptionalEmpty() {
        String message = "singleton";
        List<Object> list = Arrays.asList(message, message);

        final Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }
}