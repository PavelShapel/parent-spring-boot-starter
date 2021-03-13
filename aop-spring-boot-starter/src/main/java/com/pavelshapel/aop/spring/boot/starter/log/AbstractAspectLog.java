package com.pavelshapel.aop.spring.boot.starter.log;

import java.util.Objects;

public abstract class AbstractAspectLog {
    public static final String LOG_PATTERN = "[{}.{}] {}: {}";
    public static final String NOTHING_TO_LOG = "nothing to log";

    protected String getVerifiedLogResult(Object result) {
        return Objects.isNull(result) || result.toString().length() == 0 ? NOTHING_TO_LOG : result.toString();
    }
}
