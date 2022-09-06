package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final byte MIN = Byte.MIN_VALUE;
    private static final byte MAX = Byte.MAX_VALUE;

    public static final Specification VALUE = Specification.builder()
            .type(TYPE)
            .min(MIN)
            .max(MAX)
            .build();

    @Test
    void hashCode_WithValidParameter_ShouldReturnAppropriateValue() {
        Entity entity = createEntity();

        int result = entity.hashCode();

        assertThat(result).isPositive();
    }

    @Test
    void toString_WithValidParameter_ShouldReturnStringWithAppropriateValue() {
        Entity entity = createEntity();

        String result = entity.toString();

        assertThat(result)
                .isNotEmpty()
                .isEqualTo(String.format(
                        "Entity(super={key=Specification(type=%s, min=%d, max=%d, body=null)})",
                        TYPE,
                        MIN,
                        MAX
                ));
    }

    private static Entity createEntity() {
        Entity entity = new Entity();
        entity.put(KEY, VALUE);
        return entity;
    }
}