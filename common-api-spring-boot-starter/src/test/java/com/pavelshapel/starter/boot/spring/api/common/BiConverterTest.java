package com.pavelshapel.starter.boot.spring.api.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class BiConverterTest {
  private BiConverter<String, Integer> sut;

  @BeforeEach
  void setUp() {
    sut =
        new BiConverter<>() {
          @Override
          public Integer convertForward(String source) {
            return source == null ? null : Integer.parseInt(source);
          }

          @Override
          public String convertBackward(Integer target) {
            return target == null ? null : target.toString();
          }
        };
  }

  @Test
  void convertForwardReturnsConvertedValue() {
    String source = "42";

    Integer result = sut.convertForward(source);

    assertThat(result).isEqualTo(42);
  }

  @Test
  void convertForwardHandlesNull() {
    Integer result = sut.convertForward(/* source= */ null);

    assertThat(result).isNull();
  }

  @Test
  void convertBackwardReturnsConvertedValueWhenImplemented() {
    Integer target = 42;

    String result = sut.convertBackward(target);

    assertThat(result).isEqualTo("42");
  }

  @Test
  void convertBackwardHandlesNull() {
    String result = sut.convertBackward(/* target= */ null);

    assertThat(result).isNull();
  }

  @Test
  void convertBackwardThrowsWhenNotImplemented() {
    BiConverter<String, Integer> sut = Integer::parseInt;

    assertThatThrownBy(() -> sut.convertBackward(/* target= */ 42))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Not implemented yet");
  }

  @Test
  void reverseInvertsConversionDirection() {
    BiConverter<Integer, String> result = sut.reverse();

    assertAll(
        () -> assertThat(result.convertForward(/* source= */ 42)).isEqualTo("42"),
        () -> assertThat(result.convertBackward(/* target= */ "100")).isEqualTo(100));
  }

  @Test
  void reverseHandlesNull() {
    BiConverter<Integer, String> result = sut.reverse();

    assertAll(
        () -> assertThat(result.convertForward(/* source= */ null)).isNull(),
        () -> assertThat(result.convertBackward(/* target= */ null)).isNull());
  }

  @Test
  void reverseTwiceRestoresOriginalConversion() {
    BiConverter<String, Integer> result = sut.reverse().reverse();

    assertAll(
        () -> assertThat(result.convertForward(/* source= */ "123")).isEqualTo(123),
        () -> assertThat(result.convertBackward(/* target= */ 456)).isEqualTo("456"));
  }

  @Test
  void reverseThrowsWhenBackwardConversionNotImplemented() {
    BiConverter<String, Integer> sut = Integer::parseInt;

    BiConverter<Integer, String> result = sut.reverse();

    assertThatThrownBy(() -> result.convertForward(/* source= */ 42))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Not implemented yet");
  }
}
