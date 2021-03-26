package com.pavelshapel.web.spring.boot.starter.wrapper.provider;


import lombok.Getter;

import java.util.Date;

@Getter
public enum TestTypes {
    STRING("test"),
    DATE(new Date()),
    BOOLEAN(true),
    LONG(123L),
    DOUBLE(123.123);

    private final Object value;

    TestTypes(Object value) {
        this.value = value;
    }
}
