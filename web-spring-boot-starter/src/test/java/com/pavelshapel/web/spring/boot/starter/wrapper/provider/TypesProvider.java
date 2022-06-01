package com.pavelshapel.web.spring.boot.starter.wrapper.provider;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum TypesProvider {
    STRING("test"),
    DATE(new Date()),
    BOOLEAN(true),
    LONG(123L),
    DOUBLE(123.123);

    Object value;
}
