package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreStreamUtilsTest {
    public static final String COLLECTION_ELEMENT = "collection element";
    public static final Predicate<String> startsWithC = input -> input.startsWith("c");
    public static final Predicate<String> endsWithT = input -> input.endsWith("t");

    @Autowired
    private StreamUtils streamUtils;

    @Test
    void toOptionalList_WithValidParameter_ShouldReturnResult() {
        List<Object> list = singletonList(COLLECTION_ELEMENT);

        Optional<List<Object>> optionalList = list.stream().collect(streamUtils.toOptionalList());

        assertThat(optionalList)
                .isNotEmpty()
                .hasValue(list);
    }

    @Test
    void toSingleton_WithSingleCollection_ShouldReturnResult() {
        List<Object> list = singletonList(COLLECTION_ELEMENT);

        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton)
                .isNotEmpty()
                .hasValue(COLLECTION_ELEMENT);
    }

    @ParameterizedTest
    @EmptySource
    void toSingleton_WithEmptyCollection_ShouldReturnOptionalEmpty(List<Object> list) {
        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }

    @Test
    void toSingleton_WithMultiCollection_ShouldReturnOptionalEmpty() {
        List<Object> list = asList(COLLECTION_ELEMENT, COLLECTION_ELEMENT);

        Optional<Object> singleton = list.stream().collect(streamUtils.toSingleton());

        assertThat(singleton).isEmpty();
    }

    @Test
    void toResponseEntityList_ReverseFalse_ShouldReturnResult() {
        List<String> list = singletonList(COLLECTION_ELEMENT);

        ResponseEntity<List<String>> responseEntity = list.stream().collect(streamUtils.toResponseEntityList());

        assertThat(responseEntity.getBody())
                .asList()
                .isNotEmpty()
                .isEqualTo(list);
    }

    @Test
    void toResponseEntityList_ReverseTrue_ShouldReturnResult() {
        List<String> list = singletonList(COLLECTION_ELEMENT);

        ResponseEntity<List<String>> responseEntity = list.stream().collect(streamUtils.toResponseEntityList(true));
        List<String> responseBody = responseEntity.getBody();

        assertThat(responseBody)
                .asList()
                .isNotEmpty()
                .isEqualTo(list);
        assertThat(list.get(0))
                .isEqualTo(responseBody.get(responseBody.size() - 1));
    }

    @Test
    void toReverseList_WithTrueParameter_ShouldReturnReverseCollection() {
        List<String> list = asList(
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
    void toReverseList_WithFalseParameter_ShouldReturnNotReverseCollection() {
        List<String> list = asList(
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

    @Test
    void iterableToStream_WithValidParameter_ShouldReturnStream() {
        Iterable<String> iterable = singletonList(COLLECTION_ELEMENT);

        Stream<String> stream = streamUtils.iterableToStream(iterable);

        assertThat(stream)
                .asInstanceOf(InstanceOfAssertFactories.stream(COLLECTION_ELEMENT.getClass()))
                .hasSize(1);
    }

    @Test
    void iterableToList_WithValidParameter_ShouldReturnList() {
        Iterable<String> iterable = singletonList(COLLECTION_ELEMENT);

        List<String> list = streamUtils.iterableToList(iterable);

        assertThat(list)
                .asList()
                .hasSize(1)
                .contains(COLLECTION_ELEMENT);
    }

    @Test
    void filterStream_WithValidParameters_ShouldReturnStream() {
        List<String> filteredList = Stream.of(COLLECTION_ELEMENT)
                .filter(startsWithC)
                .filter(endsWithT)
                .toList();
        List<Predicate<String>> predicates = asList(startsWithC, endsWithT);

        List<String> resultList = streamUtils.filterStream(predicates, Stream.of(COLLECTION_ELEMENT)).toList();

        assertThat(resultList)
                .isNotNull()
                .isEqualTo(filteredList);
    }

    @Test
    void filterStream_WithNullParameters_ShouldThrowException() {
        assertThatThrownBy(() -> streamUtils.filterStream(null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void composePredicate_WithValidParameter_ShouldReturnPredicate() {
        List<Predicate<String>> predicates = asList(startsWithC, endsWithT);

        Predicate<String> predicate = streamUtils.composePredicate(predicates);

        assertThat(predicate)
                .isNotNull();
        assertThat(predicate.test(COLLECTION_ELEMENT))
                .isEqualTo(startsWithC.and(endsWithT).test(COLLECTION_ELEMENT));
    }

    @Test
    void composePredicate_WithNullParameter_ShouldThrowException() {
        assertThatThrownBy(() -> streamUtils.composePredicate(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void composePredicate_WithValidParameters_ShouldReturnPredicate() {
        Predicate<String> predicate = streamUtils.composePredicate(startsWithC, endsWithT);

        assertThat(predicate)
                .isNotNull();
        assertThat(predicate.test(COLLECTION_ELEMENT))
                .isEqualTo(startsWithC.and(endsWithT).test(COLLECTION_ELEMENT));
    }

    @Test
    void composePredicate_WithOneValidParameter_ShouldReturnPredicate() {
        Predicate<String> predicate = streamUtils.composePredicate(null, endsWithT);

        assertThat(predicate)
                .isNotNull()
                .isEqualTo(endsWithT);
    }

    @Test
    void composePredicate_WithNullParameters_ShouldThrowException() {
        assertThatThrownBy(() -> streamUtils.composePredicate(null, null))
                .isInstanceOf(NullPointerException.class);
    }
}