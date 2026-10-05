package com.pavelshapel.starter.boot.spring.api.common;

import static com.pavelshapel.starter.boot.spring.api.common.StreamCollectors.toSingle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class StreamCollectorsTest {
  @Test
  void toSingleReturnsElementWhenStreamContainsSingleElement() {
    Stream<String> stream = Stream.of("value");

    String result = stream.collect(toSingle());

    assertThat(result).isEqualTo("value");
  }

  @Test
  void toSingleReturnsNullWhenStreamContainsSingleNullElement() {
    Stream<String> stream = Stream.of((String) /* element= */ null);

    String result = stream.collect(toSingle());

    assertThat(result).isNull();
  }

  @ParameterizedTest
  @MethodSource("toSingleThrowsWhenStreamCardinalityIsNotOneTestCasesProvider")
  void toSingleThrowsWhenStreamCardinalityIsNotOne(
      ToSingleThrowsWhenStreamCardinalityIsNotOneTestCase testCase) {
    List<String> elements = testCase.elements();
    String expectedMessage = testCase.expectedMessage();
    Stream<String> stream = elements.stream();

    assertThatThrownBy(() -> stream.collect(toSingle()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(expectedMessage);
  }

  private static Stream<ToSingleThrowsWhenStreamCardinalityIsNotOneTestCase>
      toSingleThrowsWhenStreamCardinalityIsNotOneTestCasesProvider() {
    return Stream.of(
        new ToSingleThrowsWhenStreamCardinalityIsNotOneTestCase(
            List.of(), "Expected exactly 1 element, got 0"),
        new ToSingleThrowsWhenStreamCardinalityIsNotOneTestCase(
            List.of("first", "second"), "Expected exactly 1 element, got 2"));
  }

  private record ToSingleThrowsWhenStreamCardinalityIsNotOneTestCase(
      List<String> elements, String expectedMessage) {}
}
