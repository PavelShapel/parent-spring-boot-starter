package com.pavelshapel.ordered.spring.boot.starter;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderedComponentTest {
  private OrderedComponent<String, String> orderedComponent;

  @BeforeEach
  void setUp() {
    orderedComponent =
        new OrderedComponent<>() {
          @Override
          public String apply(String payload) {
            return payload;
          }
        };
  }

  @Test
  void shouldReturnTrueByDefault() {
    String payload = "testPayload";

    boolean result = orderedComponent.isApplicable(payload);

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnLowestPrecedence() {
    int result = orderedComponent.getOrder();

    assertThat(result).isEqualTo(MAX_VALUE);
  }
}
