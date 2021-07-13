package com.pavelshapel.aop.spring.boot.starter.log;

import java.util.Objects;

public abstract class AbstractAspectLog {
    public static final String LOG_PATTERN = "[{}.{}] {}: {}";
    public static final String NOTHING_TO_LOG = "nothing to log";
    public static final String SUCCESS = "success";
    public static final String THREW_AN_EXCEPTION="threw an exception";
    public static final String SUCCESS_RESULT = "returned value";
    public static final String SUCCESS_DURATION ="executed in";

    protected String getVerifiedLogResult(Object result) {
        return Objects.isNull(result) || result.toString().isEmpty() ? NOTHING_TO_LOG : result.toString();
    }
}
