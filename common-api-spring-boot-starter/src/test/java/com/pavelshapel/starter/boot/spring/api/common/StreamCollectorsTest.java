package com.pavelshapel.starter.boot.spring.api.common;

import static com.pavelshapel.starter.boot.spring.api.common.StreamCollectors.toSingle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamCollectorsTest {
  @Test
  void shouldReturnSingleElementWhenStreamContainsExactlyOneElement() {
    String result = Stream.of("value").collect(toSingle());

    assertThat(result).isEqualTo("value");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenStreamIsEmpty() {
    assertThatThrownBy(() -> Stream.<String>of().collect(toSingle()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected exactly 1 element, got 0");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenStreamContainsMultipleElements() {
    assertThatThrownBy(() -> Stream.of("first", "second").collect(toSingle()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected exactly 1 element, got 2");
  }
}
