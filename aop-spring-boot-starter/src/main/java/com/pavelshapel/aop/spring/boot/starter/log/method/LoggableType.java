package com.pavelshapel.aop.spring.boot.starter.log.method;

import lombok.Getter;

public enum LoggableType {
    METHOD_RESULT("returned value"),
    METHOD_DURATION("executed in"),
    METHOD_EXCEPTION("threw an exception");

    @Getter
    private final String prefix;

    LoggableType(String prefix) {
        this.prefix = prefix;
    }
}
