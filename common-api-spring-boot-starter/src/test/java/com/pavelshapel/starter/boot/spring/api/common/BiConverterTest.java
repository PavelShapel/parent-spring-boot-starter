package com.pavelshapel.starter.boot.spring.api.common;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class BiConverterTest {
  @Test
  void shouldConvertForwardWithStringToInteger() {
    BiConverter<String, Integer> stringToInteger = Integer::parseInt;

    Integer result = stringToInteger.convertForward("42");

    assertThat(result).isEqualTo(42);
  }

  @Test
  void shouldThrowUnsupportedOperationForBackwardWhenNotImplemented() {
    BiConverter<String, Integer> converter = Integer::parseInt;

    assertThatThrownBy(() -> converter.convertBackward(42))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Not implemented yet");
  }

  @Test
  void shouldConvertBackwardWhenImplemented() {
    BiConverter<String, Integer> stringToInteger =
        new BiConverter<>() {
          @Override
          public Integer convertForward(String source) {
            return Integer.parseInt(source);
          }

          @Override
          public String convertBackward(Integer target) {
            return target.toString();
          }
        };

    String result = stringToInteger.convertBackward(42);

    assertThat(result).isEqualTo("42");
  }

  @Test
  void shouldReverseConversionDirection() {
    BiConverter<String, Integer> stringToInteger =
        new BiConverter<>() {
          @Override
          public Integer convertForward(String source) {
            return Integer.parseInt(source);
          }

          @Override
          public String convertBackward(Integer target) {
            return target.toString();
          }
        };

    BiConverter<Integer, String> reversedConverter = stringToInteger.reverse();

    assertAll(
        () -> assertThat(reversedConverter.convertForward(42)).isEqualTo("42"),
        () -> assertThat(reversedConverter.convertBackward("100")).isEqualTo(100));
  }

  @Test
  void shouldReverseDoubleTimes() {
    BiConverter<String, Integer> original =
        new BiConverter<>() {
          @Override
          public Integer convertForward(String source) {
            return Integer.parseInt(source);
          }

          @Override
          public String convertBackward(Integer target) {
            return target.toString();
          }
        };

    BiConverter<String, Integer> doubleReversed = original.reverse().reverse();

    assertAll(
        () -> assertThat(doubleReversed.convertForward("123")).isEqualTo(123),
        () -> assertThat(doubleReversed.convertBackward(456)).isEqualTo("456"));
  }

  @Test
  void shouldHandleComplexTypeConversion() {
    BiConverter<Person, String> personToString =
        new BiConverter<>() {
          @Override
          public String convertForward(Person person) {
            return person.name() + ":" + person.age();
          }

          @Override
          public Person convertBackward(String csv) {
            String[] parts = csv.split(":");
            return new Person(parts[0], Integer.parseInt(parts[1]));
          }
        };

    Person person = new Person("Alice", 30);
    String converted = personToString.convertForward(person);

    assertAll(
        () -> assertThat(converted).isEqualTo("Alice:30"),
        () ->
            assertThat(personToString.convertBackward(converted))
                .hasFieldOrPropertyWithValue("name", "Alice")
                .hasFieldOrPropertyWithValue("age", 30));
  }

  @Test
  void shouldHandleNullInForwardConversion() {
    BiConverter<String, Integer> stringToInteger =
        source -> source == null ? null : Integer.parseInt(source);

    assertThat(stringToInteger.convertForward(null)).isNull();
  }

  @Test
  void shouldHandleNullInBackwardConversion() {
    BiConverter<String, Integer> stringToInteger =
        new BiConverter<>() {
          @Override
          public Integer convertForward(String source) {
            return Integer.parseInt(source);
          }

          @Override
          public String convertBackward(Integer target) {
            return target == null ? null : target.toString();
          }
        };

    assertThat(stringToInteger.convertBackward(null)).isNull();
  }

  @Test
  void shouldReverseWithNullHandling() {
    BiConverter<String, Integer> stringToInteger =
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

    BiConverter<Integer, String> reversed = stringToInteger.reverse();

    assertAll(
        () -> assertThat(reversed.convertForward(null)).isNull(),
        () -> assertThat(reversed.convertBackward(null)).isNull());
  }

  @Test
  void shouldConvertBetweenDifferentCollectionTypes() {
    BiConverter<int[], Integer> arrayToSum =
        new BiConverter<>() {
          @Override
          public Integer convertForward(int[] source) {
            int sum = 0;
            for (int val : source) {
              sum += val;
            }
            return sum;
          }

          @Override
          public int[] convertBackward(Integer target) {
            return new int[] {target};
          }
        };

    Integer sum = arrayToSum.convertForward(new int[] {1, 2, 3, 4, 5});

    assertAll(
        () -> assertThat(sum).isEqualTo(15),
        () -> assertThat(arrayToSum.convertBackward(10)).containsExactly(10));
  }

  private record Person(String name, int age) {}
}
