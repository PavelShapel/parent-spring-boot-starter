package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Versioned {
    String VERSION = "version";

    Long getVersion();

    void setVersion(Long version);
}
