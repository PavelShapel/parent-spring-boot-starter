package com.pavelshapel.stream.spring.boot.starter.util;

import com.pavelshapel.stream.spring.boot.starter.StreamStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        StreamStarterAutoConfiguration.class
})
class StreamUtilsTest {
    public static final String COLLECTION_ELEMENT = "collection element";

    @Autowired
    private StreamUtils streamUtils;

    @Test
    void toSingleton_WithSingleCollection_ShouldReturnResult() {
        List<Object> list = Collections.singletonList(COLLECTION_ELEMENT);

        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton)
                .isNotEmpty()
                .hasValue(COLLECTION_ELEMENT);
    }

    @Test
    void toSingleton_WithEmptyCollection_ShouldReturnOptionalEmpty() {
        List<Object> list = Collections.emptyList();

        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }

    @Test
    void toSingleton_WithMultiCollection_ShouldReturnOptionalEmpty() {
        List<Object> list = Arrays.asList(COLLECTION_ELEMENT, COLLECTION_ELEMENT);

        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }

    @Test
    void toResponseEntityList_WithCollection_ShouldReturnResult() {
        List<String> list = Collections.singletonList(COLLECTION_ELEMENT);

        ResponseEntity<List<String>> responseEntity = list.stream().collect(streamUtils.toResponseEntityList());

        assertThat(responseEntity.getBody())
                .asList()
                .isNotEmpty()
                .isEqualTo(list);
    }
}