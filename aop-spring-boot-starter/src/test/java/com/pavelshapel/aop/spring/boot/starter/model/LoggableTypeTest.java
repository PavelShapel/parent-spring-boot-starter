package com.pavelshapel.aop.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LoggableTypeTest {
    @Test
    void getPrefix_ShouldReturnNotNullValue() {
        Arrays.stream(LoggableType.values())
                .map(LoggableType::getPrefix)
                .forEach(prefix -> assertThat(prefix).isNotEmpty());
    }
}