package com.pavelshapel.ordered.spring.boot.starter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderedComponentTest {
    private OrderedComponent<String, String> orderedComponent;

    @BeforeEach
    void setUp() {
        orderedComponent = new OrderedComponent<>() {
            @Override
            public String apply(String payload) {
                return payload;
            }
        };
    }

    @Test
    void shouldReturnTrueByDefault() {
        String payload = "testPayload";

        var result = orderedComponent.isApplicable(payload);

        assertThat(result)
                .isTrue();
    }

    @Test
    void shouldReturnLowestPrecedence() {
        var result = orderedComponent.getOrder();

        assertThat(result)
                .isEqualTo(MAX_VALUE);
    }
}