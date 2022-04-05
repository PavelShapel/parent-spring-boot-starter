package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Versioned {
    Long getVersion();

    void setVersion(Long version);
}
