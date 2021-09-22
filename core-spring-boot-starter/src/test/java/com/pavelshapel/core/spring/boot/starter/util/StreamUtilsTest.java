package com.pavelshapel.core.spring.boot.starter.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
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

    @Test
    void toReverseList_WithCTrueParam_ShouldReturnReverseCollection() {
        List<String> list = Arrays.asList(
                String.format("%s%d", COLLECTION_ELEMENT, 1),
                String.format("%s%d", COLLECTION_ELEMENT, 2)
        );

        List<String> reverseList = list.stream().collect(streamUtils.toList(true));

        assertThat(reverseList)
                .asList()
                .isNotEmpty()
                .isNotEqualTo(list);
        assertThat(list.get(0))
                .isEqualTo(reverseList.get(reverseList.size() - 1));
    }

    @Test
    void toReverseList_WithCFalseParam_ShouldReturnNotReverseCollection() {
        List<String> list = Arrays.asList(
                String.format("%s%d", COLLECTION_ELEMENT, 1),
                String.format("%s%d", COLLECTION_ELEMENT, 2)
        );

        List<String> reverseList = list.stream().collect(streamUtils.toList(false));

        assertThat(reverseList)
                .asList()
                .isNotEmpty()
                .isEqualTo(list);
    }

    @Test
    void toMapOfNullables_WithoutDuplicateKeysWithoutNullValues_ShouldReturnMapWithBooleans() {
        Map<String, Boolean> map = Stream.of(Boolean.TRUE, Boolean.FALSE)
                .collect(streamUtils.toMapOfNullables(Object::toString, Function.identity(), this::and));

        map.values().forEach(value -> assertThat(value).isInstanceOf(Boolean.class));
    }

    @Test
    void toMapOfNullables_WithoutDuplicateKeysWithNullValues_ShouldReturnMapWithNulls() {
        Map<String, Boolean> map = Stream.of(Boolean.TRUE, Boolean.FALSE)
                .collect(streamUtils.toMapOfNullables(Object::toString, value -> null, this::and));

        map.values().forEach(value -> assertThat(value).isNull());
    }

    @Test
    void toMapOfNullables_WithDuplicateKeysWithFalseValues_ShouldReturnMapWithFalse() {
        Map<String, Boolean> map = Stream.of(Boolean.FALSE, Boolean.FALSE)
                .collect(streamUtils.toMapOfNullables(Object::toString, Function.identity(), this::and));

        assertThat(map)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Boolean.class))
                .hasSize(1)
                .containsEntry(Boolean.FALSE.toString(), Boolean.FALSE);
    }

    @Test
    void toMapOfNullables_WithDuplicateKeysWithTrueValues_ShouldReturnMapWithTrue() {
        Map<String, Boolean> map = Stream.of(Boolean.TRUE, Boolean.TRUE)
                .collect(streamUtils.toMapOfNullables(Object::toString, Function.identity(), this::and));

        assertThat(map)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Boolean.class))
                .hasSize(1)
                .containsEntry(Boolean.TRUE.toString(), Boolean.TRUE);
    }

    @Test
    void toMapOfNullables_WithDuplicateKeysWithTrueFalseValues_ShouldReturnMapWithFalse() {
        Map<String, Boolean> map = Stream.of(Boolean.TRUE, Boolean.FALSE)
                .collect(streamUtils.toMapOfNullables(value -> Boolean.TRUE.toString(), Function.identity(), this::and));

        assertThat(map)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Boolean.class))
                .hasSize(1)
                .containsEntry(Boolean.TRUE.toString(), Boolean.FALSE);
    }

    @Test
    void toMapOfNullables_WithDuplicateKeysWithNullValues_ShouldReturnMapWithFalse() {
        Map<String, Boolean> map = Stream.of(Boolean.TRUE, Boolean.TRUE)
                .collect(streamUtils.toMapOfNullables(Object::toString, value -> null, this::and));

        assertThat(map)
                .asInstanceOf(InstanceOfAssertFactories.map(String.class, Boolean.class))
                .hasSize(1)
                .containsEntry(Boolean.TRUE.toString(), Boolean.FALSE);
    }


    private boolean and(Boolean existing, Boolean replacement) {
        return Boolean.TRUE.equals(existing) && Boolean.TRUE.equals(replacement);
    }
}