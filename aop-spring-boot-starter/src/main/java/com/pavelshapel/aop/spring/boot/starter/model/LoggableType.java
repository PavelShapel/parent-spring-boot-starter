package com.pavelshapel.aop.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum LoggableType {
    METHOD_RESULT("returned value"),
    METHOD_DURATION("executed in"),
    METHOD_EXCEPTION("threw an exception");

    String prefix;
}
